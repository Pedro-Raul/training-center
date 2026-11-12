package co.ufps.trainingcenter.application.port.in;

import co.ufps.trainingcenter.domain.contest.Contest;
import java.time.LocalDateTime;
import java.util.List;

public interface ContestUseCase {
    List<Contest> getActiveContests();
    Contest getById(String id);
    Contest create(String title, String platform, String url, LocalDateTime startTime, int durationSeconds, boolean isCustom);
    Contest update(String id, String title, String platform, String url, LocalDateTime startTime, int durationSeconds);
    void delete(String id);
}