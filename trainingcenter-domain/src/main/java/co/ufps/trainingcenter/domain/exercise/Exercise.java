package co.ufps.trainingcenter.domain.exercise;

import java.time.LocalDateTime;
import co.ufps.trainingcenter.domain.DomainException;
import co.ufps.trainingcenter.domain.account.UserId;
import java.util.Objects;

public class Exercise {
    private final ExerciseId id;
    private String title;
    private String topic;
    private Difficulty difficulty;
    private int difficultyRating;
    private String platform;
    private String externalUrl;
    private LocalDateTime createdAt;
    private UserId createdBy;

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

    /**
     * Factory method para crear un ejercicio con creador.
     * El creador debe venir de la identidad autenticada, nunca del cliente.
     */
    public static Exercise createBy(UserId creator, String title, String topic, Difficulty difficulty,
                                    int difficultyRating, String platform, String externalUrl) {
        if (creator == null) throw new DomainException("El creador del ejercicio es obligatorio");
        requireText(title, 255, "title");
        requireText(topic, 50, "topic");
        requireText(platform, 50, "platform");
        requireText(externalUrl, 500, "externalUrl");
        if (difficulty == null) throw new DomainException("La dificultad es obligatoria");
        if (difficultyRating < 0) throw new DomainException("difficultyRating no puede ser negativo");

        Exercise exercise = new Exercise(ExerciseId.generate(), title.strip(), topic.strip(), difficulty,
                difficultyRating, platform.strip(), externalUrl.strip());
        exercise.createdBy = creator;
        return exercise;
    }

    /**
     * Restaura un ejercicio desde la base de datos incluyendo su creador
     * (puede ser null en los ejercicios históricos).
     */
    public static Exercise restore(ExerciseId id, String title, String topic, Difficulty difficulty,
                                   int difficultyRating, String platform, String externalUrl,
                                   LocalDateTime createdAt, UserId createdBy) {
        Exercise exercise = restore(id, title, topic, difficulty, difficultyRating,
                platform, externalUrl, createdAt);
        exercise.createdBy = createdBy;
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

    public UserId getCreatedBy() { return createdBy; }

    public boolean isOwnedBy(UserId user) {
        return createdBy != null && createdBy.equals(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exercise exercise = (Exercise) o;
        return Objects.equals(id, exercise.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    private static void requireText(String value, int maxLength, String field) {
        if (value == null || value.isBlank())
            throw new DomainException("El campo '" + field + "' es obligatorio");
        if (value.strip().length() > maxLength)
            throw new DomainException("El campo '" + field + "' no puede superar " + maxLength + " caracteres");
    }

}
