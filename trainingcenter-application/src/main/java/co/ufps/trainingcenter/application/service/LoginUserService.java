package co.ufps.trainingcenter.application.service;

import co.ufps.trainingcenter.domain.DomainException;
import co.ufps.trainingcenter.domain.account.InvalidCredentialsException;
import co.ufps.trainingcenter.domain.account.User;
import co.ufps.trainingcenter.domain.account.UserProfile;
import co.ufps.trainingcenter.application.port.in.LoginUserCommand;
import co.ufps.trainingcenter.application.port.in.LoginUserResult;
import co.ufps.trainingcenter.application.port.in.LoginUserUseCase;
import co.ufps.trainingcenter.application.port.out.PasswordHasher;
import co.ufps.trainingcenter.application.port.out.UserProfileRepository;
import co.ufps.trainingcenter.application.port.out.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Objects;
import java.util.Optional;

/**
 * Servicio de inicio de sesión de usuarios.
 * Valida credenciales y retorna información del usuario y su perfil.
 */
@ApplicationScoped
public final class LoginUserService implements LoginUserUseCase {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordHasher passwordHasher;

    public LoginUserService(
            UserRepository userRepository,
            UserProfileRepository userProfileRepository,
            PasswordHasher passwordHasher
    ) {
        this.userRepository = Objects.requireNonNull(userRepository, "userRepository es obligatorio");
        this.userProfileRepository = Objects.requireNonNull(userProfileRepository, "userProfileRepository es obligatorio");
        this.passwordHasher = Objects.requireNonNull(passwordHasher, "passwordHasher es obligatorio");
    }

    @Override
    public LoginUserResult login(LoginUserCommand command) {
        Objects.requireNonNull(command, "command es obligatorio");

        if (command.email() == null || command.email().isBlank()) {
            throw new DomainException("El email es obligatorio");
        }
        if (command.rawPassword() == null || command.rawPassword().isBlank()) {
            throw new DomainException("La contraseña es obligatoria");
        }

        User user = userRepository.findByEmail(command.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordHasher.verify(command.rawPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        Optional<UserProfile> profileOpt = userProfileRepository.findByUserId(user.getId());

        if (profileOpt.isEmpty()) {
            return new LoginUserResult(
                    user.getId().value(),
                    user.getEmail(),
                    user.getUsername(),
                    0, 0, 0, 0
            );
        }

        UserProfile profile = profileOpt.get();
        return new LoginUserResult(
                user.getId().value(),
                user.getEmail(),
                user.getUsername(),
                profile.getRating(),
                profile.getCurrentStreak(),
                profile.getMaxStreak(),
                profile.getProblemsSolvedCount()
        );
    }
}
