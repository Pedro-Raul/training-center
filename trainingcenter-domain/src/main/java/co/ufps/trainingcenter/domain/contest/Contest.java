package co.ufps.trainingcenter.domain.contest;

import co.ufps.trainingcenter.domain.contest.ContestId;
import co.ufps.trainingcenter.domain.contest.Status;

import java.time.LocalDateTime;
import java.util.Objects;

public class Contest {

    private final ContestId id;
    private String title;
    private String platform;
    private String url;
    private LocalDateTime startTime;
    private int durationSeconds;
    private Status status;
    private boolean isCustom;

    public Contest(ContestId id, String title, String platform, String url,
                   LocalDateTime startTime,int durationSeconds, Status status, boolean isCustom){

        this.id=id;
        this.title=title;
        this.platform=platform;
        this.url=url;
        this.startTime=startTime;
        this.durationSeconds=durationSeconds;
        this.status=status;
        this.isCustom=isCustom;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusSeconds(durationSeconds);
    }

    public void updateStatusBasedOnTime(LocalDateTime currentTime) {

        if (this.status == Status.CANCELLED) {
            return;
        }

        if (currentTime.isBefore(this.startTime)) {
            this.status = Status.UPCOMING;
        } else if (currentTime.isAfter(this.getEndTime())) {
            this.status = Status.FINISHED;
        } else {
            this.status = Status.ACTIVE;
        }
    }

    public static Contest restore(ContestId id, String title, String platform, String url,
                                  java.time.LocalDateTime startTime, int durationSeconds,
                                  Status status, boolean isCustom) {
        return new Contest(id, title, platform, url, startTime, durationSeconds, status, isCustom);
    }

    public ContestId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPlatform() {
        return platform;
    }

    public String getUrl() {
        return url;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public Status getStatus() {
        return status;
    }

    public boolean isCustom() {
        return isCustom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contest contest = (Contest) o;
        return Objects.equals(id, contest.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
