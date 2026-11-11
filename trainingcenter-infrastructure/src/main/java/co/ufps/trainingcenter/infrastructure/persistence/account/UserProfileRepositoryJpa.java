package co.ufps.trainingcenter.infrastructure.persistence.account;

import co.ufps.trainingcenter.domain.account.UserId;
import co.ufps.trainingcenter.domain.account.UserProfile;
import co.ufps.trainingcenter.application.port.out.UserProfileRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.Optional;

/**
 * Adaptador del repositorio de perfiles usando JPA/Hibernate.
 * Mapea entre la entidad de dominio (UserProfile) y la entidad JPA (UserProfileJpa).
 */
@ApplicationScoped
public class UserProfileRepositoryJpa implements UserProfileRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final UserProfileJpaMapper mapper = new UserProfileJpaMapper();

    @Override
    public Optional<UserProfile> findByUserId(UserId userId) {
        return entityManager
                .createQuery(
                        "SELECT p FROM UserProfileJpa p WHERE p.userId = :userId",
                        UserProfileJpa.class)
                .setParameter("userId", userId.value())
                .getResultList().stream().findFirst()
                .map(mapper::toDomain);
    }

    @Override
    @Transactional
    public void save(UserProfile profile) {
        entityManager.merge(mapper.toJpa(profile));
    }
}