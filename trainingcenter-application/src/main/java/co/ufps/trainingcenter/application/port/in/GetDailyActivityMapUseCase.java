package co.ufps.trainingcenter.application.port.in;

import co.ufps.trainingcenter.account.UserId;
import co.ufps.trainingcenter.activity.DailyActivity;

import java.time.LocalDate;
import java.util.List;

public interface GetDailyActivityMapUseCase {

    List<DailyActivity> getActivityMap(UserId userId, LocalDate from, LocalDate to);
}