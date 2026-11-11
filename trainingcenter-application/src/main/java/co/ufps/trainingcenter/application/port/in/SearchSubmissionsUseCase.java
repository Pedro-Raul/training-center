package co.ufps.trainingcenter.application.port.in;

import co.ufps.trainingcenter.domain.account.UserId;
import co.ufps.trainingcenter.domain.account.UserSubmission;
import co.ufps.trainingcenter.application.query.PageRequest;
import co.ufps.trainingcenter.application.query.PageResult;
import co.ufps.trainingcenter.application.query.SubmissionFilter;

public interface SearchSubmissionsUseCase {

    PageResult<UserSubmission> search(UserId userId, SubmissionFilter filter, PageRequest pageRequest);
}