package co.ufps.trainingcenter.application.port.in;

import co.ufps.trainingcenter.domain.account.UserId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Comando para actualizar el perfil de un usuario.
 */
public record UpdateProfileCommand(
        UserId userId,

        @NotBlank(message = "El nombre de usuario es obligatorio")
        @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
        String username
) {
}
