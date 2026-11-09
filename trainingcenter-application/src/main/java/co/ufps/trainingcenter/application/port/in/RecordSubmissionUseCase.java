package co.ufps.trainingcenter.application.port.in;

import co.ufps.trainingcenter.account.UserId;
import co.ufps.trainingcenter.activity.DailyActivity;

import java.time.LocalDate;

public interface RecordSubmissionUseCase {

    DailyActivity recordSubmission(UserId userId, LocalDate date, boolean solved);
}