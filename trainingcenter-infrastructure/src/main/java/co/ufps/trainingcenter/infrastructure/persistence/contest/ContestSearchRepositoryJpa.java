package co.ufps.trainingcenter.infrastructure.persistence.contest;

import co.ufps.trainingcenter.application.port.out.ContestSearchRepository;
import co.ufps.trainingcenter.application.query.ContestFilter;
import co.ufps.trainingcenter.application.query.PageRequest;
import co.ufps.trainingcenter.application.query.PageResult;
import co.ufps.trainingcenter.domain.contest.Contest;
import co.ufps.trainingcenter.infrastructure.persistence.QuerySupport;
import co.ufps.trainingcenter.infrastructure.persistence.contest.ContestJpaMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ContestSearchRepositoryJpa implements ContestSearchRepository {

    private static final Map<String, String> SORTABLE = Map.of(
            "startTime", "c.startTime",
            "title", "c.title",
            "platform", "c.platform",
            "status", "c.status",
            "durationSeconds", "c.durationSeconds");

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public PageResult<Contest> search(ContestFilter filter, PageRequest page) {
        StringBuilder where = new StringBuilder(" WHERE 1 = 1");
        Map<String, Object> params = new HashMap<>();

        if (filter.status() != null) {
            where.append(" AND c.status = :status");
            params.put("status", filter.status());
        }
        if (filter.platform() != null) {
            where.append(" AND LOWER(c.platform) = :platform");
            params.put("platform", filter.platform().toLowerCase());
        }
        if (filter.custom() != null) {
            where.append(" AND c.isCustom = :custom");
            params.put("custom", filter.custom());
        }
        if (filter.title() != null) {
            where.append(" AND LOWER(c.title) LIKE :title");
            params.put("title", "%" + filter.title().toLowerCase() + "%");
        }
        if (filter.from() != null) {
            where.append(" AND c.startTime >= :from");
            params.put("from", filter.from().atStartOfDay());
        }
        if (filter.to() != null) {
            where.append(" AND c.startTime < :to");
            params.put("to", filter.to().plusDays(1).atStartOfDay());
        }

        long total = QuerySupport.bind(entityManager.createQuery(
                        "SELECT COUNT(c) FROM ContestJpa c" + where, Long.class), params)
                .getSingleResult();

        if (total == 0 || page.offset() >= total) {
            return new PageResult<>(List.of(), page.page(), page.size(), total);
        }

        String order = QuerySupport.orderBy(SORTABLE, "startTime", page, "c.contestId");
        List<ContestJpa> rows = QuerySupport.bind(entityManager.createQuery(
                        "SELECT c FROM ContestJpa c" + where + order, ContestJpa.class), params)
                .setFirstResult(page.offset())
                .setMaxResults(page.size())
                .getResultList();

        return new PageResult<>(
                rows.stream().map(ContestJpaMapper::toDomain).toList(),
                page.page(), page.size(), total);
    }
}