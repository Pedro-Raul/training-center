package co.ufps.trainingcenter.application.port.out;

import co.ufps.trainingcenter.account.User;
import co.ufps.trainingcenter.account.UserProfile;

public interface UserRepository {

    boolean existsByEmail(String email);

    void save(User user, UserProfile profile);
}