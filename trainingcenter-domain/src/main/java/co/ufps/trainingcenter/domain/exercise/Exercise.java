package co.ufps.trainingcenter.domain.exercise;

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

    /**
     * Factory method para crear un nuevo ejercicio.
     */
    public static Exercise create(String title, String topic, Difficulty difficulty,
                                  int difficultyRating, String platform, String externalUrl) {
        return new Exercise(ExerciseId.generate(), title, topic, difficulty,
                difficultyRating, platform, externalUrl);
    }

    /**
     * Factory method para restaurar un ejercicio desde la base de datos
     * (preserva la fecha de creaci\u00f3n original).
     */
    public static Exercise restore(ExerciseId id, String title, String topic, Difficulty difficulty,
                                   int difficultyRating, String platform, String externalUrl,
                                   LocalDateTime createdAt) {
        Exercise exercise = new Exercise(id, title, topic, difficulty,
                difficultyRating, platform, externalUrl);
        exercise.createdAt = createdAt;
        return exercise;
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
