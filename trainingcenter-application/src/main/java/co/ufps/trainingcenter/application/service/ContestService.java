package co.ufps.trainingcenter.application.service;

import co.ufps.trainingcenter.application.port.in.ContestUseCase;
import co.ufps.trainingcenter.application.port.out.ContestRepository;
import co.ufps.trainingcenter.domain.contest.Contest;
import co.ufps.trainingcenter.domain.contest.Status;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Servicio de gestiÃ³n de concursos.
 * Bean CDI @ApplicationScoped para que inyecte repositorios automÃ¡ticamente.
 */
@ApplicationScoped
public class ContestService implements ContestUseCase {

    private final ContestRepository contestRepository;

    public ContestService(ContestRepository contestRepository) {
        this.contestRepository = Objects.requireNonNull(contestRepository);
    }

    @Override
    public List<Contest> getActiveContests() {
        return contestRepository.findByStatus(Status.ACTIVE);
    }

    @Override
    public void updateStatuses() {
        List<Contest> upcoming = contestRepository.findByStatus(Status.UPCOMING);
        List<Contest> active = contestRepository.findByStatus(Status.ACTIVE);

        LocalDateTime now = LocalDateTime.now();

        upcoming.forEach(c -> {
            c.updateStatusBasedOnTime(now);
            contestRepository.save(c);
        });

        active.forEach(c -> {
            c.updateStatusBasedOnTime(now);
            contestRepository.save(c);
        });
    }
}
