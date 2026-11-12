package co.ufps.trainingcenter.api.rate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Binding annotation para rate limiting en endpoints.
 * Se aplica a métodos de recursos REST para limitar el número de
 * solicitudes por ventana de tiempo.
 *
 * <p>Ejemplo de uso:
 * <pre>{@code
 * @RateLimit(maxRequests = 5, windowSeconds = 60)
 * public Response register(RegisterUserCommand command) { ... }
 * }</pre>
 * <p>El filtro {@link RateLimitFilter} detecta esta anotación
 * y aplica las restricciones correspondientes.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    /**
     * Número máximo de solicitudes permitidas en la ventana de tiempo.
     * @return número máximo de solicitudes (default: 10)
     */
    int maxRequests() default 10;

    /**
     * Duración de la ventana de tiempo en segundos.
     * @return duración de la ventana en segundos (default: 60)
     */
    int windowSeconds() default 60;
}
