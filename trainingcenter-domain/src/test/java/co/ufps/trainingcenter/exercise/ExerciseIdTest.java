package co.ufps.trainingcenter.exercise;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExerciseIdTest {

    @Test
    void shouldCreateExerciseIdFromValidString() {
        ExerciseId id = ExerciseId.of("test-uuid-123");

        assertEquals("test-uuid-123", id.value());
    }

    @Test
    void shouldGenerateRandomExerciseId() {
        ExerciseId id1 = ExerciseId.generate();
        ExerciseId id2 = ExerciseId.generate();

        assertNotNull(id1.value());
        assertNotNull(id2.value());
        assertNotEquals(id1, id2);
    }

    @Test
    void shouldThrowExceptionForNullValue() {
        assertThrows(IllegalArgumentException.class, () -> ExerciseId.of(null));
    }

    @Test
    void shouldThrowExceptionForBlankValue() {
        assertThrows(IllegalArgumentException.class, () -> ExerciseId.of(""));
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        ExerciseId id1 = ExerciseId.of("same-uuid");
        ExerciseId id2 = ExerciseId.of("same-uuid");
        ExerciseId id3 = ExerciseId.of("different-uuid");

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1, id3);
    }
}
