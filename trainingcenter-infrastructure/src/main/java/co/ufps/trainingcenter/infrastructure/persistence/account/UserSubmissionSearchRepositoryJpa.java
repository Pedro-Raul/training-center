package co.ufps.trainingcenter.infrastructure.persistence.account;

import co.ufps.trainingcenter.domain.account.UserId;
import co.ufps.trainingcenter.domain.account.UserSubmission;
import co.ufps.trainingcenter.application.port.out.UserSubmissionSearchRepository;
import co.ufps.trainingcenter.application.query.PageRequest;
import co.ufps.trainingcenter.application.query.PageResult;
import co.ufps.trainingcenter.application.query.SubmissionFilter;
<<<<<<< HEAD:trainingcenter-infrastructure/src/main/java/co/ufps/trainingcenter/infrastructure/persistence/account/UserSubmissionSearchRepositoryJpa.java
import co.ufps.trainingcenter.infrastructure.persistence.QuerySupport;
=======
import co.ufps.trainingcenter.infrastructure.persistence.account.UserSubmissionJpa;
import co.ufps.trainingcenter.infrastructure.persistence.account.UserSubmissionJpaMapper;
import co.ufps.trainingcenter.persistence.QuerySupport;
>>>>>>> 569d56e (Fixing Bugs):trainingcenter-infrastructure/src/main/java/co/ufps/trainingcenter/persistence/account/UserSubmissionSearchRepositoryJpa.java
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class UserSubmissionSearchRepositoryJpa implements UserSubmissionSearchRepository {

    private static final Map<String, String> SORTABLE = Map.of(
            "submittedAt", "s.submittedAt",
            "executionTimeMs", "s.executionTimeMs",
            "verdict", "s.verdict",
            "language", "s.language");

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public PageResult<UserSubmission> search(UserId userId, SubmissionFilter filter, PageRequest page) {
        StringBuilder where = new StringBuilder(" WHERE s.userId = :userId");
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId.value());

        if (filter.verdict() != null) {
            where.append(" AND s.verdict = :verdict");
            params.put("verdict", filter.verdict());
        }
        if (filter.language() != null) {
            where.append(" AND LOWER(s.language) = :language");
            params.put("language", filter.language().toLowerCase());
        }
        if (filter.exerciseId() != null) {
            where.append(" AND s.exerciseId = :exerciseId");
            params.put("exerciseId", filter.exerciseId());
        }
        if (filter.from() != null) {
            where.append(" AND s.submittedAt >= :from");
            params.put("from", filter.from().atStartOfDay());
        }
        if (filter.to() != null) {
            where.append(" AND s.submittedAt < :to");
            params.put("to", filter.to().plusDays(1).atStartOfDay());
        }

        long total = QuerySupport.bind(entityManager.createQuery(
                        "SELECT COUNT(s) FROM UserSubmissionJpa s" + where, Long.class), params)
                .getSingleResult();

        if (total == 0 || page.offset() >= total) {
            return new PageResult<>(List.of(), page.page(), page.size(), total);
        }

        String order = QuerySupport.orderBy(SORTABLE, "submittedAt", page, "s.submissionId");
        List<UserSubmissionJpa> rows = QuerySupport.bind(entityManager.createQuery(
                        "SELECT s FROM UserSubmissionJpa s" + where + order, UserSubmissionJpa.class), params)
                .setFirstResult(page.offset())
                .setMaxResults(page.size())
                .getResultList();

        return new PageResult<>(
                rows.stream().map(UserSubmissionJpaMapper::toDomain).toList(),
                page.page(), page.size(), total);
    }
}