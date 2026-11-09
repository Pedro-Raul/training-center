package co.ufps.trainingcenter.application.port;

import co.ufps.trainingcenter.account.UserId;
import co.ufps.trainingcenter.activity.DailyActivity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyActivityRepository {

    Optional<DailyActivity> findByUserIdAndDate(UserId userId, LocalDate date);

    List<DailyActivity> findByUserIdAndDateBetween(UserId userId, LocalDate from, LocalDate to);

    DailyActivity save(DailyActivity activity);
}