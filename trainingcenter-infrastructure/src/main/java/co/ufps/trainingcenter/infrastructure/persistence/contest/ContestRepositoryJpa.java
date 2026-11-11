package co.ufps.trainingcenter.infrastructure.persistence.contest;

import co.ufps.trainingcenter.application.port.out.ContestRepository;
import co.ufps.trainingcenter.domain.contest.Contest;
import co.ufps.trainingcenter.domain.contest.ContestId;
import co.ufps.trainingcenter.domain.contest.Status;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class ContestRepositoryJpa implements ContestRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Contest save(Contest contest) {
        entityManager.merge(toEntity(contest));
        return contest;
    }

    @Override
    public Optional<Contest> findById(ContestId id) {
        ContestJpa entity = entityManager.find(ContestJpa.class, id.value());
        return Optional.ofNullable(entity).map(this::toDomain);
    }

    @Override
    public List<Contest> findByStatus(Status status) {
        return entityManager
                .createQuery(
                        "SELECT c FROM ContestJpa c WHERE c.status = :status ORDER BY c.startTime ASC",
                        ContestJpa.class)
                .setParameter("status", status)
                .getResultList().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }



    private ContestJpa toEntity(Contest contest) {
        ContestJpa entity = new ContestJpa();
        entity.setContestId(contest.getId().value());
        entity.setTitle(contest.getTitle());
        entity.setPlatform(contest.getPlatform());
        entity.setUrl(contest.getUrl());
        entity.setStartTime(contest.getStartTime());
        entity.setDurationSeconds(contest.getDurationSeconds());
        entity.setStatus(contest.getStatus());
        entity.setCustom(contest.isCustom());
        return entity;
    }


    private Contest toDomain(ContestJpa entity) {
        return Contest.restore(
                ContestId.of(entity.getContestId()),
                entity.getTitle(),
                entity.getPlatform(),
                entity.getUrl(),
                entity.getStartTime(),
                entity.getDurationSeconds(),
                entity.getStatus(),
                entity.isCustom()
        );
    }
}
