package co.ufps.trainingcenter.exercise;

import co.ufps.trainingcenter.domain.exercise.Difficulty;
import co.ufps.trainingcenter.domain.exercise.Exercise;
import co.ufps.trainingcenter.domain.exercise.ExerciseId;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ExerciseTest {

    @Test
    void shouldCreateExerciseSuccessfully() {
        Exercise exercise = Exercise.create(
                "Two Sum",
                "Array",
                Difficulty.EASY,
                250,
                "LeetCode",
                "https://leetcode.com/problems/two-sum"
        );

        assertNotNull(exercise.getId());
        assertEquals("Two Sum", exercise.getTitle());
        assertEquals("Array", exercise.getTopic());
        assertEquals(Difficulty.EASY, exercise.getDifficulty());
        assertEquals(250, exercise.getDifficultyRating());
        assertEquals("LeetCode", exercise.getPlatform());
        assertEquals("https://leetcode.com/problems/two-sum", exercise.getExternalUrl());
        assertNotNull(exercise.getCreatedAt());
    }

    @Test
    void shouldRestoreExerciseWithOriginalCreatedAt() {
        LocalDateTime originalDate = LocalDateTime.of(2026, 1, 15, 10, 30);
        ExerciseId id = ExerciseId.of("exercise-123");

        Exercise exercise = Exercise.restore(
                id,
                "Two Sum",
                "Array",
                Difficulty.EASY,
                250,
                "LeetCode",
                "https://leetcode.com/problems/two-sum",
                originalDate
        );

        assertEquals(id, exercise.getId());
        assertEquals(originalDate, exercise.getCreatedAt());
    }

    @Test
    void shouldSupportAllDifficultyLevels() {
        Exercise easy = Exercise.create("Easy", "Array", Difficulty.EASY, 250, "LeetCode", "url");
        Exercise medium = Exercise.create("Medium", "Array", Difficulty.MEDIUM, 500, "LeetCode", "url");
        Exercise hard = Exercise.create("Hard", "Array", Difficulty.HARD, 750, "LeetCode", "url");

        assertEquals(Difficulty.EASY, easy.getDifficulty());
        assertEquals(Difficulty.MEDIUM, medium.getDifficulty());
        assertEquals(Difficulty.HARD, hard.getDifficulty());
    }
}
