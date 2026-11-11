package co.ufps.trainingcenter.application.port.out;

import co.ufps.trainingcenter.domain.exercise.Difficulty;
import co.ufps.trainingcenter.domain.exercise.Exercise;
import co.ufps.trainingcenter.domain.exercise.ExerciseId;

import java.util.List;
import java.util.Optional;

/**
 * Port de salida para persistencia de ejercicios.
 */
public interface ExerciseRepository {

    Exercise save(Exercise exercise);

    Optional<Exercise> findById(ExerciseId id);

    List<Exercise> findByDifficulty(Difficulty difficulty);

    List<Exercise> findByTopic(String topic);
}
