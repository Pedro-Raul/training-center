package co.ufps.trainingcenter.infrastructure.persistence.activity;

import co.ufps.trainingcenter.domain.account.UserId;
import co.ufps.trainingcenter.domain.activity.DailyActivity;
import co.ufps.trainingcenter.domain.activity.DailyActivityId;
import co.ufps.trainingcenter.application.port.out.DailyActivityRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador del repositorio de actividad diaria usando JPA/Hibernate.
 * Mapea entre la entidad de dominio (DailyActivity) y la entidad JPA (DailyActivityJpa).
 */
@ApplicationScoped
public class DailyActivityRepositoryJpa implements DailyActivityRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public DailyActivity save(DailyActivity activity) {
        entityManager.merge(toEntity(activity));
        return activity;
    }

    @Override
    public Optional<DailyActivity> findByUserIdAndDate(UserId userId, LocalDate date) {
        return entityManager
                .createQuery(
                        "SELECT d FROM DailyActivityJpa d WHERE d.userId = :userId AND d.activityDate = :date",
                        DailyActivityJpa.class)
                .setParameter("userId", userId.value())
                .setParameter("date", date)
                .getResultList().stream().findFirst()
                .map(this::toDomain);
    }

    @Override
    public List<DailyActivity> findByUserIdAndDateBetween(UserId userId, LocalDate from, LocalDate to) {
        return entityManager
                .createQuery(
                        "SELECT d FROM DailyActivityJpa d "
                                + "WHERE d.userId = :userId AND d.activityDate BETWEEN :from AND :to "
                                + "ORDER BY d.activityDate ASC",
                        DailyActivityJpa.class)
                .setParameter("userId", userId.value())
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    // â”€â”€ Mapeo: Domain â†’ JPA â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private DailyActivityJpa toEntity(DailyActivity activity) {
        DailyActivityJpa entity = new DailyActivityJpa();
        entity.setActivityId(activity.getActivityId().value());
        entity.setUserId(activity.getUserId().value());
        entity.setActivityDate(activity.getActivityDate());
        entity.setProblemsSolved(activity.getProblemsSolved());
        entity.setTotalSubmissions(activity.getTotalSubmissions());
        return entity;
    }

    // â”€â”€ Mapeo: JPA â†’ Domain â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private DailyActivity toDomain(DailyActivityJpa entity) {
        return DailyActivity.restore(
                DailyActivityId.of(entity.getActivityId()),
                UserId.of(entity.getUserId()),
                entity.getActivityDate(),
                entity.getProblemsSolved(),
                entity.getTotalSubmissions()
        );
    }
}
