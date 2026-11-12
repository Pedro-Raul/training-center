package co.ufps.trainingcenter.api.submission;

import co.ufps.trainingcenter.domain.account.UserId;
import co.ufps.trainingcenter.domain.account.UserSubmission;
import co.ufps.trainingcenter.domain.account.UserSubmissionId;
import co.ufps.trainingcenter.domain.account.Verdict;
import co.ufps.trainingcenter.domain.activity.DailyActivity;
import co.ufps.trainingcenter.application.port.in.RecordSubmissionUseCase;
import co.ufps.trainingcenter.application.port.out.UserSubmissionRepository;
import co.ufps.trainingcenter.domain.exercise.ExerciseId;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Endpoint REST para registro de submissions.
 */
@Path("/api/v1/submissions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SubmissionResource {

    public record SubmissionCommand(
            @NotBlank(message = "El ID del ejercicio es obligatorio")
            String exerciseId,
            @NotNull(message = "El veredicto es obligatorio")
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
    private final UserSubmissionRepository userSubmissionRepository;

    @Inject
    public SubmissionResource(RecordSubmissionUseCase recordSubmissionUseCase,
                              UserSubmissionRepository userSubmissionRepository) {
        this.recordSubmissionUseCase = recordSubmissionUseCase;
        this.userSubmissionRepository = userSubmissionRepository;
    }

    @POST
    @Path("/{userId}")
    public SubmissionResponse record(@PathParam("userId") String userId,
                                     @jakarta.validation.Valid SubmissionCommand command) {
        boolean solved = command.verdict() == Verdict.ACCEPTED;

        UserSubmission submission = new UserSubmission(
                UserSubmissionId.generate(),
                UserId.of(userId),
                ExerciseId.of(command.exerciseId()),
                command.verdict(),
                command.language(),
                command.executionTimeMs() != null ? command.executionTimeMs() : 0,
                LocalDateTime.now());

        userSubmissionRepository.save(submission);

        DailyActivity activity = recordSubmissionUseCase.recordSubmission(
                UserId.of(userId),
                LocalDate.now(),
                solved);

        return SubmissionResponse.from(submission);
    }
}
