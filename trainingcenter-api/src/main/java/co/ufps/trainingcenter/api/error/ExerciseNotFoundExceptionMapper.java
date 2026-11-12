package co.ufps.trainingcenter.api.error;

import co.ufps.trainingcenter.domain.exercise.ExerciseNotFoundException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ExerciseNotFoundExceptionMapper implements ExceptionMapper<ExerciseNotFoundException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(ExerciseNotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorResponse("NOT_FOUND", exception.getMessage(),
                        uriInfo != null ? uriInfo.getPath() : null))
                .build();
    }
}