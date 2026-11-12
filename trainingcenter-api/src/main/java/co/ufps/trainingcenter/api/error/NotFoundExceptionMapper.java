package co.ufps.trainingcenter.api.error;

import co.ufps.trainingcenter.domain.NotFoundException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Mapper para excepciones de recurso no encontrado.
 * Devuelve HTTP 404 NOT FOUND con ErrorResponse estandarizado.
 *
 * Tiene prioridad sobre DomainExceptionMapper porque NotFoundException
 * es una subclase de DomainException y JAX-RS selecciona el mapper más específico.
 */
@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(NotFoundException exception) {
        ErrorResponse body = new ErrorResponse(
                "NOT_FOUND",
                exception.getMessage(),
                uriInfo != null ? uriInfo.getPath() : null
        );
        return Response.status(Response.Status.NOT_FOUND)
                .entity(body)
                .build();
    }
}
