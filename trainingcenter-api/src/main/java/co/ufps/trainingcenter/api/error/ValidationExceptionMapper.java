package co.ufps.trainingcenter.api.error;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Mapper global para excepciones de validación de Bean Validation.
 * Convierte ConstraintViolationException en respuestas HTTP 400 con un formato
 * que incluye las violaciones individuales por campo.
 *
 * <p>Ejemplo de respuesta:
 * <pre>{@code
 * {
 *   "timestamp": "2026-11-11T18:00:00",
 *   "status": 400,
 *   "error": "Validation Failed",
 *   "violations": {
 *     "email": "El email debe tener un formato válido",
 *     "username": "El nombre de usuario es obligatorio"
 *   }
 * }
 * }</pre>
 */
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        Map<String, Object> errorResponse = new LinkedHashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        errorResponse.put("status", Response.Status.BAD_REQUEST.getStatusCode());
        errorResponse.put("error", "Validation Failed");

        // Extrae los mensajes de validación individuales agrupados por propiedad
        Map<String, String> violations = new HashMap<>();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            String property = violation.getPropertyPath().toString();
            violations.merge(property, violation.getMessage(), (existing, newMsg) ->
                    existing + "; " + newMsg);
        }
        errorResponse.put("violations", violations);

        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(errorResponse)
                .build();
    }
}