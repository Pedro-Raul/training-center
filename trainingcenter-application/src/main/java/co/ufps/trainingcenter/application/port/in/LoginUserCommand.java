package co.ufps.trainingcenter.application.port.in;

/**
 * Comando para iniciar sesión con email y contraseña.
 */
public record LoginUserCommand(
        String email,
        String rawPassword
) {
}
