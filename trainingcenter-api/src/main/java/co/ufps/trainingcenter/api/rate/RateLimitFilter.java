package co.ufps.trainingcenter.api.rate;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Filtro JAX-RS que implementa rate limiting basado en dirección IP.
 *
 * <p>Verifica si el método del recurso tiene la anotación {@link RateLimit}
 * y aplica el límite correspondiente. Si no tiene la anotación, la solicitud
 * se procesa sin restricciones.
 *
 * <p>Utiliza un algoritmo de contadores fijos (fixed window) con
 * almacenamiento en memoria. Para producción, considere usar Redis
 * o un mecanismo distribuido.
 *
 * <p>La prioridad {@link Priorities#AUTHENTICATION} asegura que este
 * filtro se ejecute temprano en el ciclo de vida de la petición.
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class RateLimitFilter implements ContainerRequestFilter {

    @Context
    private HttpHeaders httpHeaders;

    @Context
    private ResourceInfo resourceInfo;

    /**
     * Mapa de IP -> contador de ventana.
     */
    private final Map<String, WindowCounter> counters = new ConcurrentHashMap<>();

    /**
     * Contador para una ventana de tiempo específica.
     */
    private static class WindowCounter {
        private volatile long windowStart;
        private final AtomicInteger requestCount;

        WindowCounter(long windowStart, AtomicInteger requestCount) {
            this.windowStart = windowStart;
            this.requestCount = requestCount;
        }

        long getWindowStart() { return windowStart; }
        AtomicInteger getRequestCount() { return requestCount; }
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        Method method = resourceInfo.getResourceMethod();
        RateLimit annotation = method.getAnnotation(RateLimit.class);

        // Si el método no tiene @RateLimit, se omite el filtro
        if (annotation == null) {
            annotation = resourceInfo.getResourceClass().getAnnotation(RateLimit.class);
            if (annotation == null) return;
        }

        int maxRequests = annotation.maxRequests();
        int windowSeconds = annotation.windowSeconds();

        String clientIp = getClientIp();
        long now = System.currentTimeMillis();
        long windowMillis = windowSeconds * 1000L;

        WindowCounter counter = counters.compute(clientIp, (key, existing) -> {
            if (existing == null || (now - existing.getWindowStart()) > windowMillis) {
                return new WindowCounter(now, new AtomicInteger(0));
            }
            return existing;
        });

        // Verifica si la ventana expiró y la reinicia si es necesario
        if ((now - counter.getWindowStart()) > windowMillis) {
            counter = new WindowCounter(now, new AtomicInteger(0));
            counters.put(clientIp, counter);
        }

        // Incrementa y verifica el límite
        int currentCount = counter.getRequestCount().incrementAndGet();
        if (currentCount > maxRequests) {
            counters.remove(clientIp);
            requestContext.abortWith(
                    Response.status(Response.Status.TOO_MANY_REQUESTS)
                            .header("Retry-After", String.valueOf(windowSeconds))
                            .entity(Map.of(
                                    "error", "rate_limit_exceeded",
                                    "message", "Demasiadas solicitudes. Intente nuevamente en " + windowSeconds + " segundos.",
                                    "maxRequests", maxRequests,
                                    "windowSeconds", windowSeconds
                            ))
                            .build()
            );
            return;
        }

        // Limpieza periódica de entradas antiguas (cada 1000 solicitudes)
        if (currentCount % 1000 == 0) {
            cleanupExpiredEntries(now, windowMillis);
        }
    }

    /**
     * Obtiene la dirección IP del cliente, considerando encabezados de proxy.
     */
    private String getClientIp() {
        String xForwardedFor = httpHeaders.getHeaderString("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        String xRealIp = httpHeaders.getHeaderString("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        return "unknown";
    }

    /**
     * Elimina entradas expiradas del mapa de contadores.
     */
    private void cleanupExpiredEntries(long now, long windowMillis) {
        counters.entrySet().removeIf(entry ->
                (now - entry.getValue().getWindowStart()) > windowMillis
        );
    }
}
