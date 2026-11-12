package co.ufps.trainingcenter.application.port.in;

import co.ufps.trainingcenter.domain.exercise.Exercise;

public interface CreateExerciseUseCase {
    Exercise createExercise(CreateExerciseCommand command);
}