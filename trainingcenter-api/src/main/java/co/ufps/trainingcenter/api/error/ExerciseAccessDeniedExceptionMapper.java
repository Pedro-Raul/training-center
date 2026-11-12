package co.ufps.trainingcenter.api.error;

import co.ufps.trainingcenter.domain.exercise.ExerciseAccessDeniedException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ExerciseAccessDeniedExceptionMapper implements ExceptionMapper<ExerciseAccessDeniedException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(ExerciseAccessDeniedException exception) {
        return Response.status(Response.Status.FORBIDDEN)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorResponse("FORBIDDEN", exception.getMessage(),
                        uriInfo != null ? uriInfo.getPath() : null))
                .build();
    }
}