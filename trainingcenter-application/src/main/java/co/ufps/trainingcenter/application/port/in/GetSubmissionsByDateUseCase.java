package co.ufps.trainingcenter.application.port.in;

import co.ufps.trainingcenter.account.SubmissionCountByDate;
import co.ufps.trainingcenter.account.UserId;
import java.time.LocalDate;
import java.util.List;

public interface GetSubmissionsByDateUseCase {

    List<SubmissionCountByDate> getSubmissionsByDate(UserId userId, LocalDate from, LocalDate to);
}