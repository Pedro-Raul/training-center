package co.ufps.trainingcenter.application.service;

import co.ufps.trainingcenter.domain.DomainException;
import co.ufps.trainingcenter.domain.account.User;
import co.ufps.trainingcenter.domain.account.UserProfile;
import co.ufps.trainingcenter.application.port.in.LoginUserResult;
import co.ufps.trainingcenter.application.port.in.UpdateProfileCommand;
import co.ufps.trainingcenter.application.port.in.UpdateProfileUseCase;
import co.ufps.trainingcenter.application.port.out.UserProfileRepository;
import co.ufps.trainingcenter.application.port.out.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Objects;
import java.util.Optional;

/**
 * Servicio para actualizar el perfil de un usuario.
 * Permite cambiar el nombre de usuario visible.
 */
@ApplicationScoped
public final class UpdateProfileService implements UpdateProfileUseCase {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    public UpdateProfileService(
            UserRepository userRepository,
            UserProfileRepository userProfileRepository
    ) {
        this.userRepository = Objects.requireNonNull(userRepository, "userRepository es obligatorio");
        this.userProfileRepository = Objects.requireNonNull(userProfileRepository, "userProfileRepository es obligatorio");
    }

    @Override
    public LoginUserResult updateProfile(UpdateProfileCommand command) {
        Objects.requireNonNull(command, "command es obligatorio");

        if (command.username() == null || command.username().isBlank()) {
            throw new DomainException("El nombre de usuario es obligatorio");
        }

        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new DomainException("Usuario no encontrado"));

        // Reconstruir el usuario con el nuevo username
        User updatedUser = User.restore(
                user.getId(),
                user.getEmail(),
                command.username(),
                user.getPasswordHash(),
                user.getRole(),
                user.getCreatedAt()
        );

        userRepository.save(updatedUser);

        Optional<UserProfile> profileOpt = userProfileRepository.findByUserId(command.userId());

        if (profileOpt.isEmpty()) {
            return new LoginUserResult(
                    updatedUser.getId().value(),
                    updatedUser.getEmail(),
                    updatedUser.getUsername(),
                    0, 0, 0, 0
            );
        }

        UserProfile profile = profileOpt.get();
        return new LoginUserResult(
                updatedUser.getId().value(),
                updatedUser.getEmail(),
                updatedUser.getUsername(),
                profile.getRating(),
                profile.getCurrentStreak(),
                profile.getMaxStreak(),
                profile.getProblemsSolvedCount()
        );
    }
}
