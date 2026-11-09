package co.ufps.trainingcenter.application.service;

import co.ufps.trainingcenter.account.UserId;
import co.ufps.trainingcenter.activity.DailyActivity;
import co.ufps.trainingcenter.application.port.DailyActivityRepository;
import co.ufps.trainingcenter.DomainException;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class DailyActivityService {

    private final DailyActivityRepository dailyActivityRepository;

    public DailyActivityService(DailyActivityRepository dailyActivityRepository) {
        this.dailyActivityRepository = Objects.requireNonNull(dailyActivityRepository);
    }

    public DailyActivity recordSubmission(UserId userId, LocalDate date, boolean solved) {
        DailyActivity activity = dailyActivityRepository
                .findByUserIdAndDate(userId, date)
                .orElseGet(() -> DailyActivity.startOf(userId, date));

        activity.recordSubmission(solved);
        return dailyActivityRepository.save(activity);
    }

    public List<DailyActivity> getActivityMap(UserId userId, LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new DomainException("from no puede ser posterior a to");
        }
        return dailyActivityRepository.findByUserIdAndDateBetween(userId, from, to);
    }
}