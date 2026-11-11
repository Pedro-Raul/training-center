package co.ufps.trainingcenter.application.port.out;

import co.ufps.trainingcenter.domain.account.User;
import co.ufps.trainingcenter.domain.account.UserId;
import co.ufps.trainingcenter.domain.account.UserProfile;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(UserId userId);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    // guarda el usuario con su perfil o ninguno de los dos
    void save(User user, UserProfile profile);

    // guarda solo el usuario (sin perfil)
    void save(User user);
}