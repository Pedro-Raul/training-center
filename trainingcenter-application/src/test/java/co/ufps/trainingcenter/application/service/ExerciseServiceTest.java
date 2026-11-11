package co.ufps.trainingcenter.application.service;

import co.ufps.trainingcenter.application.port.out.ExerciseRepository;
import co.ufps.trainingcenter.domain.exercise.Difficulty;
import co.ufps.trainingcenter.domain.exercise.Exercise;
import co.ufps.trainingcenter.domain.exercise.ExerciseId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExerciseServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private ExerciseService exerciseService;

    @Test
    void shouldReturnExercisesByTopic() {
        String topic = "Array";
        Exercise exercise = Exercise.restore(
                ExerciseId.of("ex-1"),
                "Two Sum",
                "Array",
                Difficulty.EASY,
                250,
                "LeetCode",
                "https://leetcode.com/problems/two-sum",
                null
        );

        when(exerciseRepository.findByTopic(topic)).thenReturn(List.of(exercise));

        List<Exercise> result = exerciseService.getExercisesByTopic(topic);

        assertEquals(1, result.size());
        assertEquals("Two Sum", result.get(0).getTitle());
        verify(exerciseRepository).findByTopic(topic);
    }

    @Test
    void shouldReturnEmptyListWhenNoExercisesFound() {
        when(exerciseRepository.findByTopic("NonExistent")).thenReturn(List.of());

        List<Exercise> result = exerciseService.getExercisesByTopic("NonExistent");

        assertTrue(result.isEmpty());
    }
}
