package co.ufps.trainingcenter.application.port.in;

import co.ufps.trainingcenter.domain.account.UserId;
import co.ufps.trainingcenter.domain.activity.DailyActivity;

import java.time.LocalDate;

public interface RecordSubmissionUseCase {

    DailyActivity recordSubmission(UserId userId, LocalDate date, boolean solved);
}