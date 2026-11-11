package co.ufps.trainingcenter.application.port.in;

import co.ufps.trainingcenter.application.query.ContestFilter;
import co.ufps.trainingcenter.application.query.PageRequest;
import co.ufps.trainingcenter.application.query.PageResult;
import co.ufps.trainingcenter.domain.contest.Contest;

public interface SearchContestsUseCase {

    PageResult<Contest> search(ContestFilter filter, PageRequest pageRequest);
}
