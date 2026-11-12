package co.ufps.trainingcenter.api.error;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(WebApplicationException exception) {
        int status = exception.getResponse().getStatus();
        Response.Status known = Response.Status.fromStatusCode(status);
        String code = known != null ? known.name() : "HTTP_" + status;
        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorResponse(code, exception.getMessage(),
                        uriInfo != null ? uriInfo.getPath() : null))
                .build();
    }
}