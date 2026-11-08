package co.ufps.trainingcenter.contest;

import java.time.LocalDateTime;

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
}
