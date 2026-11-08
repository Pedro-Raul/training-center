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
}
