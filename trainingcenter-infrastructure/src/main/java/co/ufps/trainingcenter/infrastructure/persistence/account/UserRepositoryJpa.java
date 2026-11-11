package co.ufps.trainingcenter.infrastructure.persistence.account;

import co.ufps.trainingcenter.domain.account.User;
import co.ufps.trainingcenter.domain.account.UserId;
import co.ufps.trainingcenter.domain.account.UserProfile;
import co.ufps.trainingcenter.application.port.out.UserRepository;
import co.ufps.trainingcenter.infrastructure.persistence.account.UserJpa;
import co.ufps.trainingcenter.infrastructure.persistence.account.UserJpaMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.Optional;

/**
 * Adaptador del repositorio de usuarios usando JPA/Hibernate.
 * Mapea entre la entidad de dominio (User) y la entidad JPA (UserJpa).
 */
@ApplicationScoped
public class UserRepositoryJpa implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final UserJpaMapper mapper = new UserJpaMapper();
    public Optional<User> findById(UserId userId) {
        return Optional.ofNullable(entityManager.find(UserJpa.class, userId.value()))
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        UserJpa found = entityManager
                .createQuery(
                        "SELECT u FROM UserJpa u WHERE u.email = :email",
                        UserJpa.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst()
                .orElse(null);
        return found != null;
    }

    @Override
    @Transactional
    public void save(User user, UserProfile profile) {
        entityManager.persist(mapper.toJpa(user));
        entityManager.persist(mapper.toProfileJpa(profile));
    }
}
