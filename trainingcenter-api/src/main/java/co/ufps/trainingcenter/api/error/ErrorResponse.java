package co.ufps.trainingcenter.api.error;

import java.time.Instant;

/**
 * Respuesta de error estandarizada para toda la API.
 * Incluye código de error, mensaje legible, ruta y timestamp.
 */
public record ErrorResponse(
        String error,
        String message,
        String path,
        Instant timestamp
) {

    public ErrorResponse(String error, String message, String path) {
        this(error, message, path, Instant.now());
    }
}
