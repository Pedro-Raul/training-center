package co.ufps.trainingcenter.api.exercise;

import co.ufps.trainingcenter.application.port.in.GetExercisesUseCase;
import co.ufps.trainingcenter.domain.exercise.Difficulty;
import co.ufps.trainingcenter.domain.exercise.Exercise;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Endpoint REST para consulta de ejercicios.
 * Retorna DTOs; nunca expone entidades de dominio directamente.
 */
@Path("/api/exercises")
@Produces(MediaType.APPLICATION_JSON)
public class ExerciseResource {

    @Inject
    private GetExercisesUseCase getExercisesUseCase;

    @GET
    public List<ExerciseResponse> getExercises(@QueryParam("topic") String topic) {
        return getExercisesUseCase.getExercisesByTopic(topic).stream()
                .map(ExerciseResponse::from)
                .collect(Collectors.toList());
    }

    public record ExerciseResponse(
            String id,
            String title,
            String topic,
            Difficulty difficulty,
            int difficultyRating,
            String platform,
            String externalUrl,
            LocalDateTime createdAt) {

        static ExerciseResponse from(Exercise exercise) {
            return new ExerciseResponse(
                    exercise.getId().value(),
                    exercise.getTitle(),
                    exercise.getTopic(),
                    exercise.getDifficulty(),
                    exercise.getDifficultyRating(),
                    exercise.getPlatform(),
                    exercise.getExternalUrl(),
                    exercise.getCreatedAt());
        }
    }
}
