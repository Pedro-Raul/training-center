package co.ufps.trainingcenter.persistence.account;


import co.ufps.trainingcenter.account.Verdict;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "USER_SUBMISSIONS")
public class UserSubmissionJpa {

    @Id
    @Column(name = "submission_id", length = 36)
    private String submissionId;

    @Column(name = "user_id", length = 36)
    private String userId;

    @Column(name = "exercise_id", length = 36)
    private String exerciseId;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private Verdict verdict;

    @Column(length = 20)
    private String language;

    @Column(name = "execution_time_ms")
    private int executionTimeMs;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    public String getSubmissionId() { return submissionId;}

    public void setSubmissionId(String submissionId) { this.submissionId = submissionId;}

    public String getUserId() { return userId;}

    public void setUserId(String userId) { this.userId = userId;}

    public String getExerciseId() { return exerciseId;}

    public void setExerciseId(String exerciseId) { this.exerciseId = exerciseId; }

    public Verdict getVerdict() { return verdict; }

    public void setVerdict(Verdict verdict) { this.verdict = verdict; }

    public String getLanguage() { return language; }

    public void setLanguage(String language) { this.language = language; }

    public int getExecutionTimeMs() {return executionTimeMs;}

    public void setExecutionTimeMs(int executionTimeMs) {this.executionTimeMs = executionTimeMs;}

    public LocalDateTime getSubmittedAt() {return submittedAt;}

    public void setSubmittedAt(LocalDateTime submittedAt) {this.submittedAt = submittedAt;}
}
