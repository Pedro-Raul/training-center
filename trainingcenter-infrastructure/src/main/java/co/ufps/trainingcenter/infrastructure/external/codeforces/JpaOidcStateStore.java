package co.ufps.trainingcenter.infrastructure.external.codeforces;

import co.ufps.trainingcenter.application.port.out.OidcStateStore;
import co.ufps.trainingcenter.application.port.out.PendingLink;
import co.ufps.trainingcenter.infrastructure.persistence.oidc.OidcStateJpa;
import co.ufps.trainingcenter.infrastructure.persistence.oidc.OidcStateJpaMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import io.quarkus.scheduler.Scheduled;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@ApplicationScoped
public class JpaOidcStateStore implements OidcStateStore {

    private static final Duration TTL = Duration.ofMinutes(10);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void save(String state, PendingLink pending) {
        Instant expiresAt = Instant.now().plus(TTL);
        PendingLink withTtl = new PendingLink(pending.userId(), pending.nonce(), expiresAt);
        OidcStateJpa entity = OidcStateJpaMapper.toJpa(state, withTtl);
        entityManager.persist(entity);
    }

    @Override
    @Transactional
    public Optional<PendingLink> consume(String state) {
        OidcStateJpa entity = entityManager.find(OidcStateJpa.class, state);
        if (entity == null) {
            return Optional.empty();
        }
        if (entity.getExpiresAt().isBefore(Instant.now())) {
            entityManager.remove(entity);
            return Optional.empty();
        }
        PendingLink pending = OidcStateJpaMapper.toPendingLink(entity);
        entityManager.remove(entity);
        return Optional.of(pending);
    }

    @Scheduled(every = "5m")
    @Transactional
    public void cleanupExpired() {
        entityManager.createQuery(
                        "DELETE FROM OidcStateJpa s WHERE s.expiresAt < :now")
                .setParameter("now", Instant.now())
                .executeUpdate();
    }
}
