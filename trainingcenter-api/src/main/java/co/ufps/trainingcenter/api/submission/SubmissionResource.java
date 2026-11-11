package co.ufps.trainingcenter.api.submission;

import co.ufps.trainingcenter.domain.account.UserId;
import co.ufps.trainingcenter.domain.account.UserSubmission;
import co.ufps.trainingcenter.domain.account.Verdict;
import co.ufps.trainingcenter.application.port.in.RecordSubmissionUseCase;
import co.ufps.trainingcenter.domain.exercise.ExerciseId;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.time.LocalDateTime;

/**
 * Endpoint REST para registro de submissions.
 * Solo delega al use case; no accede repositorios directamente.
 */
@Path("/api/submissions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SubmissionResource {

    public record SubmissionCommand(
            String exerciseId,
            Verdict verdict,
            String language,
            Integer executionTimeMs) {
    }

    public record SubmissionResponse(
            String submissionId,
            String exerciseId,
            Verdict verdict,
            String language,
            Integer executionTimeMs,
            LocalDateTime submittedAt) {

        static SubmissionResponse from(UserSubmission submission) {
            return new SubmissionResponse(
                    submission.getId().value(),
                    submission.getExerciseId().value(),
                    submission.getVerdict(),
                    submission.getLanguage(),
                    submission.getExecutionTimeMs(),
                    submission.getSubmittedAt());
        }
    }

    private final RecordSubmissionUseCase recordSubmissionUseCase;

    @Inject
    public SubmissionResource(RecordSubmissionUseCase recordSubmissionUseCase) {
        this.recordSubmissionUseCase = recordSubmissionUseCase;
    }

    @POST
    @Path("/{userId}")
    public SubmissionResponse record(@PathParam("userId") String userId,
                                     SubmissionCommand command) {
        UserSubmission submission = recordSubmissionUseCase.recordSubmission(
                UserId.of(userId),
                ExerciseId.of(command.exerciseId()),
                command.verdict().name(),
                command.language(),
                command.executionTimeMs() != null ? command.executionTimeMs() : 0,
                LocalDateTime.now());

        return SubmissionResponse.from(submission);
    }
}
