package co.ufps.trainingcenter.application.port.in;

import co.ufps.trainingcenter.domain.account.UserId;
import co.ufps.trainingcenter.domain.account.UserSubmission;
import co.ufps.trainingcenter.domain.exercise.ExerciseId;

import java.time.LocalDateTime;

public interface RecordSubmissionUseCase {

    UserSubmission recordSubmission(UserId userId, ExerciseId exerciseId,
                                    String verdict, String language,
                                    int executionTimeMs, LocalDateTime submittedAt);
}
