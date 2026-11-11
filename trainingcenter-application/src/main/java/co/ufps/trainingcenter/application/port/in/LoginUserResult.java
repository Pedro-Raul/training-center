package co.ufps.trainingcenter.application.port.in;

/**
 * Resultado del inicio de sesión exitoso.
 * Contiene la información del usuario y su perfil.
 */
public record LoginUserResult(
        String userId,
        String email,
        String username,
        int rating,
        int currentStreak,
        int maxStreak,
        int problemsSolvedCount
) {
}
