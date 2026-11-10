package co.ufps.trainingcenter.application.port.in;

import co.ufps.trainingcenter.exercise.Exercise;

import java.util.List;

/**
 * UseCase para consulta de ejercicios.
 */
public interface GetExercisesUseCase {

    List<Exercise> getExercisesByTopic(String topic);
}
