package co.ufps.trainingcenter.api.exercise;

import co.ufps.trainingcenter.api.security.AuthenticatedUser;
import co.ufps.trainingcenter.application.port.in.DeleteExerciseUseCase;
import co.ufps.trainingcenter.domain.exercise.ExerciseId;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.PathParam;
import co.ufps.trainingcenter.application.port.in.GetExerciseByIdUseCase;
import co.ufps.trainingcenter.application.port.in.CreateExerciseCommand;
import co.ufps.trainingcenter.application.port.in.CreateExerciseUseCase;
import co.ufps.trainingcenter.application.port.in.GetExercisesUseCase;
import co.ufps.trainingcenter.domain.account.UserId;
import co.ufps.trainingcenter.domain.exercise.Difficulty;
import co.ufps.trainingcenter.domain.exercise.Exercise;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.time.LocalDateTime;
import java.util.List;

@Path("/api/v1/exercises")
@Produces(MediaType.APPLICATION_JSON)
public class ExerciseResource {

    public record CreateExerciseRequest(
            @NotBlank(message = "El título es obligatorio") @Size(max = 255) String title,
            @NotBlank(message = "El tema es obligatorio") @Size(max = 50) String topic,
            @NotNull(message = "La dificultad es obligatoria") Difficulty difficulty,
            @PositiveOrZero(message = "El rating no puede ser negativo") int difficultyRating,
            @NotBlank(message = "La plataforma es obligatoria") @Size(max = 50) String platform,
            @NotBlank(message = "La URL es obligatoria") @Size(max = 500) String externalUrl
    ) {}

    public record ExerciseResponse(
            String id, String title, String topic, Difficulty difficulty,
            int difficultyRating, String platform, String externalUrl,
            LocalDateTime createdAt, String createdBy
    ) {
        static ExerciseResponse from(Exercise exercise) {
            return new ExerciseResponse(
                    exercise.getId().value(),
                    exercise.getTitle(),
                    exercise.getTopic(),
                    exercise.getDifficulty(),
                    exercise.getDifficultyRating(),
                    exercise.getPlatform(),
                    exercise.getExternalUrl(),
                    exercise.getCreatedAt(),
                    exercise.getCreatedBy() == null ? null : exercise.getCreatedBy().value());
        }
    }

    @Inject
    private GetExercisesUseCase getExercisesUseCase;

    @Inject
    private GetExerciseByIdUseCase getExerciseByIdUseCase;

    @Inject
    private DeleteExerciseUseCase deleteExerciseUseCase;

    @Inject
    private CreateExerciseUseCase createExerciseUseCase;

    @GET
    public List<Exercise> getExercises(
            @QueryParam("topic") String topic) {
        return getExercisesUseCase.getExercisesByTopic(topic);
    }

    @GET
    @Path("/{exerciseId}")
    @AuthenticatedUser
    public ExerciseResponse getById(@PathParam("exerciseId") String exerciseId) {
        return ExerciseResponse.from(getExerciseByIdUseCase.getExerciseById(ExerciseId.of(exerciseId)));
    }

    @POST
    @AuthenticatedUser
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@Valid CreateExerciseRequest request,
                           @Context SecurityContext securityContext) {
        UserId creator = UserId.of(securityContext.getUserPrincipal().getName());
        Exercise created = createExerciseUseCase.createExercise(new CreateExerciseCommand(
                creator,
                request.title(),
                request.topic(),
                request.difficulty(),
                request.difficultyRating(),
                request.platform(),
                request.externalUrl()));
        return Response.status(Response.Status.CREATED)
                .entity(ExerciseResponse.from(created))
                .build();
    }

    @DELETE
    @Path("/{exerciseId}")
    @AuthenticatedUser
    public Response delete(@PathParam("exerciseId") String exerciseId,
                           @Context SecurityContext securityContext) {
        UserId requester = UserId.of(securityContext.getUserPrincipal().getName());
        deleteExerciseUseCase.deleteExercise(ExerciseId.of(exerciseId), requester);
        return Response.noContent().build();
    }

}