package co.ufps.trainingcenter.application.service;

import co.ufps.trainingcenter.DomainException;
import co.ufps.trainingcenter.account.UserId;
import co.ufps.trainingcenter.account.UserProfile;
import co.ufps.trainingcenter.activity.DailyActivity;
import co.ufps.trainingcenter.application.port.in.RecordSubmissionUseCase;
import co.ufps.trainingcenter.application.port.out.DailyActivityRepository;
import co.ufps.trainingcenter.application.port.out.UserProfileRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class DailyActivityService implements RecordSubmissionUseCase {

    private final DailyActivityRepository dailyActivityRepository;
    private final UserProfileRepository userProfileRepository;

    public DailyActivityService(
            DailyActivityRepository dailyActivityRepository,
            UserProfileRepository userProfileRepository
    ) {
        this.dailyActivityRepository = Objects.requireNonNull(dailyActivityRepository);
        this.userProfileRepository = Objects.requireNonNull(userProfileRepository);
    }

    @Override
    public DailyActivity recordSubmission(UserId userId, LocalDate date, boolean solved) {
        UserProfile profile = null;

        if (solved) {
            profile = userProfileRepository.findByUserId(userId)
                    .orElseThrow(() -> new DomainException("No existe perfil para el usuario"));

            profile.recordSolvedActivity(date);
        }

        DailyActivity activity = dailyActivityRepository
                .findByUserIdAndDate(userId, date)
                .orElseGet(() -> DailyActivity.startOf(userId, date));

        activity.recordSubmission(solved);
        DailyActivity savedActivity = dailyActivityRepository.save(activity);

        if (solved) {
            userProfileRepository.save(profile);
        }

        return savedActivity;
    }

    public List<DailyActivity> getActivityMap(UserId userId, LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new DomainException("from no puede ser posterior a to");
        }
        return dailyActivityRepository.findByUserIdAndDateBetween(userId, from, to);
    }
}