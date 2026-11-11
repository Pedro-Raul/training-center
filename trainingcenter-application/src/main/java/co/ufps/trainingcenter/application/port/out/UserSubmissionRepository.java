package co.ufps.trainingcenter.application.port.out;

import co.ufps.trainingcenter.account.SubmissionCountByDate;
import co.ufps.trainingcenter.account.UserSubmission;
import co.ufps.trainingcenter.account.UserId;
import java.time.LocalDateTime;
import java.util.List;

public interface UserSubmissionRepository {

    List<SubmissionCountByDate> countByUserIdAndDateBetween(UserId userId, LocalDateTime start, LocalDateTime endExclusive);

    void save(UserSubmission submission);
}