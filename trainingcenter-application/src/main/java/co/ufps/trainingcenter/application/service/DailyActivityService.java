package co.ufps.trainingcenter.application.service;

import co.ufps.trainingcenter.domain.DomainException;
import co.ufps.trainingcenter.domain.account.UserId;
import co.ufps.trainingcenter.domain.account.UserProfile;
import co.ufps.trainingcenter.domain.account.UserSubmission;
import co.ufps.trainingcenter.domain.account.UserSubmissionId;
import co.ufps.trainingcenter.domain.account.Verdict;
import co.ufps.trainingcenter.domain.activity.DailyActivity;
import co.ufps.trainingcenter.application.port.in.GetDailyActivityMapUseCase;
import co.ufps.trainingcenter.application.port.in.RecordSubmissionUseCase;
import co.ufps.trainingcenter.application.port.out.DailyActivityRepository;
import co.ufps.trainingcenter.application.port.out.UserProfileRepository;
import co.ufps.trainingcenter.application.port.out.UserSubmissionRepository;
import co.ufps.trainingcenter.domain.exercise.ExerciseId;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Servicio de actividad diaria y registro de submissions.
 * Bean CDI @ApplicationScoped para que inyecte repositorios automÃ¡ticamente.
 */
@ApplicationScoped
public class DailyActivityService implements RecordSubmissionUseCase, GetDailyActivityMapUseCase {

    private final DailyActivityRepository dailyActivityRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserSubmissionRepository userSubmissionRepository;

    public DailyActivityService(
            DailyActivityRepository dailyActivityRepository,
            UserProfileRepository userProfileRepository,
            UserSubmissionRepository userSubmissionRepository
    ) {
        this.dailyActivityRepository = Objects.requireNonNull(dailyActivityRepository);
        this.userProfileRepository = Objects.requireNonNull(userProfileRepository);
        this.userSubmissionRepository = Objects.requireNonNull(userSubmissionRepository);
    }

    @Override
    public UserSubmission recordSubmission(UserId userId, ExerciseId exerciseId,
                                           String verdict, String language,
                                           int executionTimeMs, LocalDateTime submittedAt) {
        if (userId == null) {
            throw new DomainException("El usuario es obligatorio");
        }
        if (exerciseId == null) {
            throw new DomainException("El ejercicio es obligatorio");
        }

        boolean solved = Verdict.ACCEPTED.name().equals(verdict);

        // 1. Crear y guardar la submission
        UserSubmission submission = new UserSubmission(
                UserSubmissionId.generate(),
                userId,
                exerciseId,
                Verdict.valueOf(verdict),
                language,
                executionTimeMs,
                submittedAt);
        userSubmissionRepository.save(submission);

        // 2. Actualizar perfil si fue resuelto
        if (solved) {
            UserProfile profile = userProfileRepository.findByUserId(userId)
                    .orElseThrow(() -> new DomainException("No existe perfil para el usuario"));
            profile.recordSolvedActivity(submittedAt.toLocalDate());
            userProfileRepository.save(profile);
        }

        // 3. Registrar actividad diaria
        LocalDate date = submittedAt.toLocalDate();
        DailyActivity activity = dailyActivityRepository
                .findByUserIdAndDate(userId, date)
                .orElseGet(() -> DailyActivity.startOf(userId, date));

        activity.recordSubmission(solved);
        dailyActivityRepository.save(activity);

        return submission;
    }
    @Override
    public List<DailyActivity> getActivityMap(UserId userId, LocalDate from, LocalDate to) {
        if (userId == null) {
            throw new DomainException("El usuario es obligatorio");
        }
        if (from == null || to == null) {
            throw new DomainException("El rango de fechas es obligatorio");
        }
        if (from.isAfter(to)) {
            throw new DomainException("from no puede ser posterior a to");
        }
        return dailyActivityRepository.findByUserIdAndDateBetween(userId, from, to);
    }
}