package co.ufps.trainingcenter.application.port.in;

import co.ufps.trainingcenter.contest.Contest;

import java.util.List;

/**
 * UseCase para gestión de concursos.
 */
public interface ContestUseCase {

    List<Contest> getActiveContests();

    void updateStatuses();
}
