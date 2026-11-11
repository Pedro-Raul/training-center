package co.ufps.trainingcenter.application.port.in;

import co.ufps.trainingcenter.account.UserId;
import co.ufps.trainingcenter.account.UserSubmission;
import co.ufps.trainingcenter.application.query.PageRequest;
import co.ufps.trainingcenter.application.query.PageResult;
import co.ufps.trainingcenter.application.query.SubmissionFilter;

public interface SearchSubmissionsUseCase {

    PageResult<UserSubmission> search(UserId userId, SubmissionFilter filter, PageRequest pageRequest);
}