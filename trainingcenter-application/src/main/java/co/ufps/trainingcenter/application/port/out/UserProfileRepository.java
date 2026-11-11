package co.ufps.trainingcenter.application.port.out;

import co.ufps.trainingcenter.domain.account.UserId;
import co.ufps.trainingcenter.domain.account.UserProfile;

import java.util.Optional;

public interface UserProfileRepository {

    Optional<UserProfile> findByUserId(UserId userId);

    void save(UserProfile profile);
}