package co.ufps.trainingcenter.api.exercise;

import co.ufps.trainingcenter.application.port.in.GetExercisesUseCase;
import co.ufps.trainingcenter.domain.exercise.Exercise;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

/**
 * Endpoint REST para consulta de ejercicios.
 */
@Path("/api/v1/exercises")
@Produces(MediaType.APPLICATION_JSON)
public class ExerciseResource {

    @Inject
    private GetExercisesUseCase getExercisesUseCase;

    @GET
    public List<Exercise> getExercises(@QueryParam("topic") String topic) {
        return getExercisesUseCase.getExercisesByTopic(topic);
    }
}
