package co.ufps.trainingcenter.application.port.out;

import co.ufps.trainingcenter.application.query.ContestFilter;
import co.ufps.trainingcenter.application.query.PageRequest;
import co.ufps.trainingcenter.application.query.PageResult;
import co.ufps.trainingcenter.domain.contest.Contest;

public interface ContestSearchRepository {

    PageResult<Contest> search(ContestFilter filter, PageRequest pageRequest);
}