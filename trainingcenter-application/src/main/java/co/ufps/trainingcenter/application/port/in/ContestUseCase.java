package co.ufps.trainingcenter.application.port.in;

import co.ufps.trainingcenter.domain.contest.Contest;

import java.util.List;

/**
 * UseCase para gestiÃ³n de concursos.
 */
public interface ContestUseCase {

    List<Contest> getActiveContests();

    void updateStatuses();
}
