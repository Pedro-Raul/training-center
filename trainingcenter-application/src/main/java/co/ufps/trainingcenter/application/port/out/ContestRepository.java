package co.ufps.trainingcenter.application.port.out;

import co.ufps.trainingcenter.domain.contest.Contest;
import co.ufps.trainingcenter.domain.contest.ContestId;
import co.ufps.trainingcenter.domain.contest.Status;

import java.util.List;
import java.util.Optional;

public interface ContestRepository {


        Contest save(Contest contest);
        Optional<Contest> findById(ContestId id);
        List<Contest> findByStatus(Status status);

}
