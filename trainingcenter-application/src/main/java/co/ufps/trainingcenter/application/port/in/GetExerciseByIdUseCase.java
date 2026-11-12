package co.ufps.trainingcenter.application.port.in;

import co.ufps.trainingcenter.domain.exercise.Exercise;
import co.ufps.trainingcenter.domain.exercise.ExerciseId;

public interface GetExerciseByIdUseCase {
    Exercise getExerciseById(ExerciseId id);
}