package co.ufps.trainingcenter.application.port.in;

import co.ufps.trainingcenter.domain.account.UserId;
import co.ufps.trainingcenter.domain.activity.DailyActivity;

import java.time.LocalDate;
import java.util.List;

public interface GetDailyActivityMapUseCase {

    List<DailyActivity> getActivityMap(UserId userId, LocalDate from, LocalDate to);
}