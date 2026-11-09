package co.ufps.trainingcenter.persistence.contest;

import co.ufps.trainingcenter.contest.Status;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "CONTEST")
public class ContestJpa {

    @Id
    @Column(name = "contest_id", length = 36)
    private String contestId;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(length = 50)
    private String platform;

    @Column(length = 500)
    private String url;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "duration_seconds")
    private int durationSeconds;

    @Enumerated(EnumType.STRING) // <-- Guarda el nombre del Enum en texto ("ACTIVE", "FINISHED")
    @Column(length = 20)
    private Status status;

    @Column(name = "is_custom")
    private boolean isCustom;

    public String getContestId() {return contestId;}

    public void setContestId(String contestId) {this.contestId = contestId;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public String getPlatform() {return platform;}

    public void setPlatform(String platform) {this.platform = platform;}

    public String getUrl() {return url;}

    public void setUrl(String url) {this.url = url;}

    public LocalDateTime getStartTime() {return startTime;}

    public void setStartTime(LocalDateTime startTime) {this.startTime = startTime;}

    public int getDurationSeconds() {return durationSeconds;}

    public void setDurationSeconds(int durationSeconds) {this.durationSeconds = durationSeconds;}

    public Status getStatus() {return status;}

    public void setStatus(Status status) {this.status = status;}

    public boolean isCustom() {return isCustom;}

    public void setCustom(boolean custom) {isCustom = custom;}
}