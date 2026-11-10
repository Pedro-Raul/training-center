package co.ufps.trainingcenter.application.service;

import co.ufps.trainingcenter.application.port.in.GetExercisesUseCase;
import co.ufps.trainingcenter.application.port.out.ExerciseRepository;
import co.ufps.trainingcenter.exercise.Exercise;

import java.util.List;

/**
 * Servicio para consulta de ejercicios.
 */
public class ExerciseService implements GetExercisesUseCase {

    private final ExerciseRepository exerciseRepository;

    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public List<Exercise> getExercisesByTopic(String topic) {
        return exerciseRepository.findByTopic(topic);
    }
}
