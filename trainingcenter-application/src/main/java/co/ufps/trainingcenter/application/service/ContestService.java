package co.ufps.trainingcenter.application.service;

import co.ufps.trainingcenter.application.port.in.ContestUseCase;
import co.ufps.trainingcenter.application.port.out.ContestRepository;
import co.ufps.trainingcenter.contest.Contest;
import co.ufps.trainingcenter.contest.Status;

import java.time.LocalDateTime;
import java.util.List;

public class ContestService implements ContestUseCase {

    private final ContestRepository contestRepository;

    public ContestService(ContestRepository contestRepository) {
        this.contestRepository = contestRepository;
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
