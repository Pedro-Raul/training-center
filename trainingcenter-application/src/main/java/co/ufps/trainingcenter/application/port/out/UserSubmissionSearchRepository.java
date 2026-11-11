package co.ufps.trainingcenter.application.port.out;

import co.ufps.trainingcenter.domain.account.UserId;
import co.ufps.trainingcenter.domain.account.UserSubmission;
import co.ufps.trainingcenter.application.query.PageRequest;
import co.ufps.trainingcenter.application.query.PageResult;
import co.ufps.trainingcenter.application.query.SubmissionFilter;

public interface UserSubmissionSearchRepository {

    PageResult<UserSubmission> search(UserId userId, SubmissionFilter filter, PageRequest pageRequest);
}
