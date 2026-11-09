package co.ufps.trainingcenter.account;

import co.ufps.trainingcenter.account.UserId;
import co.ufps.trainingcenter.account.Verdict;
import co.ufps.trainingcenter.account.UserSubmissionId;
import co.ufps.trainingcenter.exercise.ExerciseId;

import java.time.LocalDateTime;

public class UserSubmission {
    private final UserSubmissionId id;
    private UserId userId;
    private ExerciseId exerciseId;
    private Verdict verdict;
    private String language;
    private int executionTimeMs;
    private LocalDateTime submittedAt;

    public UserSubmission(UserSubmissionId id, UserId userId, ExerciseId exerciseId,
                          Verdict verdict, String language, int executionTimeMs,
                          LocalDateTime submittedAt) {
        this.id = id;
        this.userId = userId;
        this.exerciseId = exerciseId;
        this.verdict = verdict;
        this.language = language;
        this.executionTimeMs = executionTimeMs;
        this.submittedAt = submittedAt;
    }

    public UserSubmissionId getId() {
        return id;
    }

    public UserId getUserId() {
        return userId;
    }

    public ExerciseId getExerciseId() {
        return exerciseId;
    }

    public Verdict getVerdict() {
        return verdict;
    }

    public String getLanguage() {
        return language;
    }

    public int getExecutionTimeMs() {
        return executionTimeMs;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }
}