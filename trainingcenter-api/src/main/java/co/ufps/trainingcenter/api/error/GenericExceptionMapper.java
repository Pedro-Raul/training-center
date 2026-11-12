package co.ufps.trainingcenter.api.error;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Mapper genérico para excepciones no previstas.
 * Devuelve HTTP 500 INTERNAL SERVER ERROR con ErrorResponse estandarizado.
 *
 * No expone detalles internos al cliente para evitar filtración de información sensible.
 */
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOG = Logger.getLogger(GenericExceptionMapper.class.getName());

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(Throwable exception) {
        LOG.log(Level.SEVERE, "Error inesperado en la API", exception);

        ErrorResponse body = new ErrorResponse(
                "INTERNAL_ERROR",
                "Ocurrió un error interno. Contacta al administrador del servicio.",
                uriInfo != null ? uriInfo.getPath() : null
        );
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(body)
                .build();
    }
}
