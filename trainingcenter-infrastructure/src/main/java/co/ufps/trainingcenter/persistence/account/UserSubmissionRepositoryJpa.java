package co.ufps.trainingcenter.persistence.account;


import co.ufps.trainingcenter.account.SubmissionCountByDate;
import co.ufps.trainingcenter.account.UserSubmission;
import co.ufps.trainingcenter.application.port.out.UserSubmissionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import co.ufps.trainingcenter.account.UserId;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class UserSubmissionRepositoryJpa implements UserSubmissionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<SubmissionCountByDate> countByUserIdAndDateBetween(
            UserId userId, LocalDateTime start, LocalDateTime endExclusive) {

        List<Object[]> rows = entityManager
                .createQuery(
                        "SELECT YEAR(s.submittedAt), MONTH(s.submittedAt), DAY(s.submittedAt), COUNT(s) "
                                + "FROM UserSubmissionJpa s "
                                + "WHERE s.userId = :userId "
                                + "AND s.submittedAt >= :start AND s.submittedAt < :end "
                                + "GROUP BY YEAR(s.submittedAt), MONTH(s.submittedAt), DAY(s.submittedAt) "
                                + "ORDER BY YEAR(s.submittedAt), MONTH(s.submittedAt), DAY(s.submittedAt)",
                        Object[].class)
                .setParameter("userId", userId.value())
                .setParameter("start", start)
                .setParameter("end", endExclusive)
                .getResultList();

        return rows.stream()
                .map(r -> new SubmissionCountByDate(
                        LocalDate.of(
                                ((Number) r[0]).intValue(),
                                ((Number) r[1]).intValue(),
                                ((Number) r[2]).intValue()),
                        ((Number) r[3]).longValue()))
                .toList();
    }

    @Override
    public void save(UserSubmission submission) {
        UserSubmissionJpa entity = new UserSubmissionJpa();
        entity.setSubmissionId(submission.getId().value());
        entity.setUserId(submission.getUserId().value());
        entity.setExerciseId(submission.getExerciseId().value());
        entity.setVerdict(submission.getVerdict());
        entity.setLanguage(submission.getLanguage());
        entity.setExecutionTimeMs(submission.getExecutionTimeMs());
        entity.setSubmittedAt(submission.getSubmittedAt());
        entityManager.persist(entity);
    }
}