package co.ufps.trainingcenter.application.service;

import co.ufps.trainingcenter.application.port.in.DeleteExerciseUseCase;
import co.ufps.trainingcenter.domain.account.UserId;
import co.ufps.trainingcenter.application.port.in.GetExerciseByIdUseCase;
import co.ufps.trainingcenter.domain.exercise.ExerciseId;
import co.ufps.trainingcenter.domain.exercise.ExerciseNotFoundException;
import co.ufps.trainingcenter.domain.exercise.ExerciseAccessDeniedException;
import co.ufps.trainingcenter.domain.exercise.ExerciseId;
import co.ufps.trainingcenter.domain.exercise.ExerciseNotFoundException;
import co.ufps.trainingcenter.application.port.in.GetExercisesUseCase;
import co.ufps.trainingcenter.application.port.in.CreateExerciseUseCase;
import co.ufps.trainingcenter.application.port.in.CreateExerciseCommand;
import co.ufps.trainingcenter.domain.DomainException;
import co.ufps.trainingcenter.application.port.out.ExerciseRepository;
import co.ufps.trainingcenter.domain.exercise.Exercise;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

/**
 * Servicio para consulta de ejercicios.
 * Bean CDI @ApplicationScoped para que inyecte repositorios automÃ¡ticamente.
 */
@ApplicationScoped
public class ExerciseService implements GetExercisesUseCase, CreateExerciseUseCase, DeleteExerciseUseCase, GetExerciseByIdUseCase {

    private final ExerciseRepository exerciseRepository;

    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public List<Exercise> getExercisesByTopic(String topic) {
        return exerciseRepository.findByTopic(topic);
    }

    @Override
    public Exercise createExercise(CreateExerciseCommand command) {
        if (command == null) {
            throw new DomainException("Los datos del ejercicio son obligatorios");
        }
        Exercise exercise = Exercise.createBy(
                command.creatorId(),
                command.title(),
                command.topic(),
                command.difficulty(),
                command.difficultyRating(),
                command.platform(),
                command.externalUrl());
        return exerciseRepository.save(exercise);
    }

    @Override
    public void deleteExercise(ExerciseId exerciseId, UserId requesterId) {
        if (exerciseId == null || requesterId == null) {
            throw new DomainException("El ejercicio y el usuario son obligatorios");
        }
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(ExerciseNotFoundException::new);
        if (!exercise.isOwnedBy(requesterId)) {
            throw new ExerciseAccessDeniedException();
        }
        exerciseRepository.deleteById(exerciseId);
    }

    @Override
    public Exercise getExerciseById(ExerciseId id) {
        if (id == null) {
            throw new DomainException("El id del ejercicio es obligatorio");
        }
        return exerciseRepository.findById(id).orElseThrow(ExerciseNotFoundException::new);
    }
}
