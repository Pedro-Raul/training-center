package co.ufps.trainingcenter.application.port.in;

public record RegisterUserCommand(
        String email,
        String username,
        String rawPassword
) {
}