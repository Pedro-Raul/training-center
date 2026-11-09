package co.ufps.trainingcenter.application.port.out;

import co.ufps.trainingcenter.account.UserId;
import co.ufps.trainingcenter.account.UserProfile;

import java.util.Optional;

public interface UserProfileRepository {

    Optional<UserProfile> findByUserId(UserId userId);

    void save(UserProfile profile);
}