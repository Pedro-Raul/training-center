package co.ufps.trainingcenter.application.service;

import co.ufps.trainingcenter.application.port.in.GetExercisesUseCase;
import co.ufps.trainingcenter.application.port.out.ExerciseRepository;
import co.ufps.trainingcenter.domain.exercise.Exercise;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

/**
 * Servicio para consulta de ejercicios.
 * Bean CDI @ApplicationScoped para que inyecte repositorios automÃ¡ticamente.
 */
@ApplicationScoped
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
