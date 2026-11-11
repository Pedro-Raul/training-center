package co.ufps.trainingcenter.application.port.in;

import co.ufps.trainingcenter.domain.account.UserId;

/**
 * Comando para actualizar el perfil de un usuario.
 */
public record UpdateProfileCommand(
        UserId userId,
        String username
) {
}
