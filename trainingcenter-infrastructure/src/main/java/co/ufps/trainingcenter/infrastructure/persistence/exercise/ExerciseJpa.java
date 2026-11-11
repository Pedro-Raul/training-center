package co.ufps.trainingcenter.infrastructure.persistence.exercise;

import co.ufps.trainingcenter.domain.exercise.Difficulty;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "exercises")
public class ExerciseJpa {

    @Id
    @Column(name = "exercise_id", length = 36)
    private String exerciseId;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(length = 100)
    private String topic;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Difficulty difficulty;

    @Column(name = "difficulty_rating")
    private int difficultyRating;

    @Column(length = 50)
    private String platform;

    @Column(name = "external_url", length = 500)
    private String externalUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Constructor requerido por JPA
    public ExerciseJpa() {}

    public String getExerciseId() { return exerciseId; }

    public void setExerciseId(String exerciseId) { this.exerciseId = exerciseId; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getTopic() { return topic; }

    public void setTopic(String topic) { this.topic = topic; }

    public Difficulty getDifficulty() { return difficulty; }

    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }

    public int getDifficultyRating() { return difficultyRating; }

    public void setDifficultyRating(int difficultyRating) { this.difficultyRating = difficultyRating; }

    public String getPlatform() { return platform; }

    public void setPlatform(String platform) { this.platform = platform; }

    public String getExternalUrl() { return externalUrl; }

    public void setExternalUrl(String externalUrl) { this.externalUrl = externalUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
