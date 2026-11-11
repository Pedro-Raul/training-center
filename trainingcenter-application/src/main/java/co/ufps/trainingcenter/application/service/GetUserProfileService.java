package co.ufps.trainingcenter.application.service;

import co.ufps.trainingcenter.domain.DomainException;
import co.ufps.trainingcenter.domain.account.User;
import co.ufps.trainingcenter.domain.account.UserId;
import co.ufps.trainingcenter.domain.account.UserProfile;
import co.ufps.trainingcenter.application.port.in.GetUserProfileUseCase;
import co.ufps.trainingcenter.application.port.in.LoginUserResult;
import co.ufps.trainingcenter.application.port.out.UserProfileRepository;
import co.ufps.trainingcenter.application.port.out.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Objects;
import java.util.Optional;

/**
 * Servicio para obtener el perfil completo de un usuario.
 */
@ApplicationScoped
public final class GetUserProfileService implements GetUserProfileUseCase {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    public GetUserProfileService(
            UserRepository userRepository,
            UserProfileRepository userProfileRepository
    ) {
        this.userRepository = Objects.requireNonNull(userRepository, "userRepository es obligatorio");
        this.userProfileRepository = Objects.requireNonNull(userProfileRepository, "userProfileRepository es obligatorio");
    }

    @Override
    public LoginUserResult getUserProfile(UserId userId) {
        Objects.requireNonNull(userId, "userId es obligatorio");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DomainException("Usuario no encontrado"));

        Optional<UserProfile> profileOpt = userProfileRepository.findByUserId(userId);

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
