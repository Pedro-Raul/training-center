package co.ufps.trainingcenter.application.service;

import co.ufps.trainingcenter.domain.DomainException;
import co.ufps.trainingcenter.domain.account.EmailAlreadyExistsException;
import co.ufps.trainingcenter.domain.account.User;
import co.ufps.trainingcenter.domain.account.UserId;
import co.ufps.trainingcenter.domain.account.UserProfile;
import co.ufps.trainingcenter.application.port.in.RegisterUserCommand;
import co.ufps.trainingcenter.application.port.in.RegisterUserUseCase;
import co.ufps.trainingcenter.application.port.out.PasswordHasher;
import co.ufps.trainingcenter.application.port.out.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Objects;

/**
 * Servicio de registro de usuarios.
 * Bean CDI @ApplicationScoped para que inyecte repositorios automÃ¡ticamente.
 */
@ApplicationScoped
public final class RegisterUserService implements RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public RegisterUserService(
            UserRepository userRepository,
            PasswordHasher passwordHasher
    ) {
        this.userRepository = Objects.requireNonNull(userRepository, "userRepository es obligatorio");
        this.passwordHasher = Objects.requireNonNull(passwordHasher, "passwordHasher es obligatorio");
    }

    @Override
    public UserId register(RegisterUserCommand command) {
        Objects.requireNonNull(command, "command es obligatorio");

        if (command.email() == null || command.email().isBlank()) {
            throw new DomainException("El email es obligatorio");
        }
        if (command.username() == null || command.username().isBlank()) {
            throw new DomainException("El nombre de usuario es obligatorio");
        }
        if (command.rawPassword() == null || command.rawPassword().isBlank()) {
            throw new DomainException("La contraseÃ±a es obligatoria");
        }

        if (userRepository.existsByEmail(command.email())) {
            throw new EmailAlreadyExistsException(command.email());
        }

        String passwordHash = passwordHasher.hash(command.rawPassword());
        User user = User.register(
                command.email(),
                command.username(),
                passwordHash,
                command.username()
        );
        UserProfile profile = UserProfile.generate(user.getId());

        userRepository.save(user, profile);
        return user.getId();
    }
}