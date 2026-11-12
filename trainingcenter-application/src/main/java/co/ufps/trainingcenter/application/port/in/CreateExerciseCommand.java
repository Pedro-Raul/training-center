package co.ufps.trainingcenter.application.port.in;

import co.ufps.trainingcenter.domain.account.UserId;
import co.ufps.trainingcenter.domain.exercise.Difficulty;

public record CreateExerciseCommand(
        UserId creatorId,
        String title,
        String topic,
        Difficulty difficulty,
        int difficultyRating,
        String platform,
        String externalUrl
) {}