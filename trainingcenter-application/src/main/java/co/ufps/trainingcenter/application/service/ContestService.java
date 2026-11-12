package co.ufps.trainingcenter.application.service;

import co.ufps.trainingcenter.application.port.in.ContestUseCase;
import co.ufps.trainingcenter.application.port.out.ContestRepository;
import co.ufps.trainingcenter.domain.DomainException;
import co.ufps.trainingcenter.domain.contest.Contest;
import co.ufps.trainingcenter.domain.contest.ContestId;
import co.ufps.trainingcenter.domain.contest.Status;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
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
    public Contest getById(String id) {
        return contestRepository.findById(ContestId.of(id))
                .orElseThrow(() -> new DomainException("Concurso no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public Contest create(String title, String platform, String url, LocalDateTime startTime, int durationSeconds, boolean isCustom) {
        ContestId id = ContestId.of(UUID.randomUUID().toString());
        Contest contest = new Contest(id, title, platform, url, startTime, durationSeconds, Status.UPCOMING, isCustom);
        contest.updateStatusBasedOnTime(LocalDateTime.now());
        return contestRepository.save(contest);
    }

    @Override
    @Transactional
    public Contest update(String id, String title, String platform, String url, LocalDateTime startTime, int durationSeconds) {
        Contest contest = getById(id);

        if (contest.getStatus() == Status.FINISHED) {
            throw new DomainException("No se puede actualizar un concurso que ya ha finalizado.");
        }

        contest.setTitle(title);
        contest.setPlatform(platform);
        contest.setUrl(url);
        contest.setStartTime(startTime);
        contest.setDurationSeconds(durationSeconds);

        contest.updateStatusBasedOnTime(LocalDateTime.now());
        return contestRepository.save(contest);
    }

    @Override
    @Transactional
    public void delete(String id) {
        Contest contest = getById(id);
        contestRepository.delete(contest.getId());
    }
}