package co.ufps.trainingcenter.application.service;

import co.ufps.trainingcenter.application.port.out.ContestRepository;
import co.ufps.trainingcenter.contest.Contest;
import co.ufps.trainingcenter.contest.Status;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class ContestService {

    private final ContestRepository contestRepository;

    public ContestService(ContestRepository contestRepository) {
        this.contestRepository = contestRepository;
    }

    public List<Contest> getActiveContests() {
        return contestRepository.findByStatus(Status.ACTIVE);
    }

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