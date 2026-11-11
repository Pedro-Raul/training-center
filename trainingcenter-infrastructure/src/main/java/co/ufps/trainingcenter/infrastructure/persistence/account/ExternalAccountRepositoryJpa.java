package co.ufps.trainingcenter.infrastructure.persistence.account;

import co.ufps.trainingcenter.domain.account.ExternalAccount;
import co.ufps.trainingcenter.domain.account.UserId;
import co.ufps.trainingcenter.application.port.out.ExternalAccountRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.Optional;

@ApplicationScoped
public class ExternalAccountRepositoryJpa implements ExternalAccountRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final ExternalAccountJpaMapper mapper = new ExternalAccountJpaMapper();

    @Override
    public Optional<ExternalAccount> findByUserIdAndPlatform(UserId userId, String platform) {
        return entityManager
                .createQuery(
                        "SELECT a FROM ExternalAccountJpa a "
                                + "WHERE a.userId = :userId AND a.platform = :platform",
                        ExternalAccountJpa.class)
                .setParameter("userId", userId.value())
                .setParameter("platform", platform)
                .getResultList().stream().findFirst()
                .map(mapper::toDomain);
    }

    @Override
    public Optional<ExternalAccount> findByPlatformAndExternalId(String platform, String externalId) {
        return entityManager
                .createQuery(
                        "SELECT a FROM ExternalAccountJpa a "
                                + "WHERE a.platform = :platform AND a.externalId = :externalId",
                        ExternalAccountJpa.class)
                .setParameter("platform", platform)
                .setParameter("externalId", externalId)
                .getResultList().stream().findFirst()
                .map(mapper::toDomain);
    }

    @Override
    @Transactional
    public ExternalAccount save(ExternalAccount account) {
        return mapper.toDomain(entityManager.merge(mapper.toJpa(account)));
    }
}