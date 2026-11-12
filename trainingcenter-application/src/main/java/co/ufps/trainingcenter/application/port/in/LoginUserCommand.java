package co.ufps.trainingcenter.application.port.in;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Comando para iniciar sesión con email y contraseña.
 */
public record LoginUserCommand(
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email debe tener un formato válido")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        String rawPassword
) {
}
