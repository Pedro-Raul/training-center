package co.ufps.trainingcenter.exercise;

import java.time.LocalDateTime;

public class Exercise {
    private final ExerciseId id;
    private String title;
    private String topic;
    private Difficulty difficulty;
    private int difficultyRating;
    private String platform;
    private String externalUrl;
    private LocalDateTime createdAt;

    private Exercise(ExerciseId id, String title, String topic, Difficulty difficulty,
                     int difficultyRating, String platform, String externalUrl) {
        this.id = id;
        this.title = title;
        this.topic = topic;
        this.difficulty = difficulty;
        this.difficultyRating = difficultyRating;
        this.platform = platform;
        this.externalUrl = externalUrl;
        this.createdAt = LocalDateTime.now();
    }

    public ExerciseId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTopic() {
        return topic;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public int getDifficultyRating() {
        return difficultyRating;
    }

    public String getPlatform() {
        return platform;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
