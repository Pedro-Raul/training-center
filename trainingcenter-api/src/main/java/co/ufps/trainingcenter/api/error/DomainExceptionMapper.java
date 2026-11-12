package co.ufps.trainingcenter.api.error;

import co.ufps.trainingcenter.domain.DomainException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Mapper global para excepciones de dominio.
 * Devuelve HTTP 400 BAD REQUEST con ErrorResponse estandarizado.
 */
@Provider
public class DomainExceptionMapper implements ExceptionMapper<DomainException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(DomainException exception) {
        ErrorResponse body = new ErrorResponse(
                "DOMAIN_ERROR",
                exception.getMessage(),
                uriInfo != null ? uriInfo.getPath() : null
        );
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(body)
                .build();
    }
}