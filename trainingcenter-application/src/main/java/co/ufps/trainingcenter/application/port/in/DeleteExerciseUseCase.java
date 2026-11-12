package co.ufps.trainingcenter.application.port.in;

import co.ufps.trainingcenter.domain.account.UserId;
import co.ufps.trainingcenter.domain.exercise.ExerciseId;

public interface DeleteExerciseUseCase {
    void deleteExercise(ExerciseId exerciseId, UserId requesterId);
}