package co.ufps.trainingcenter.api.user;

import co.ufps.trainingcenter.account.UserId;
import co.ufps.trainingcenter.application.port.in.RegisterUserCommand;
import co.ufps.trainingcenter.application.port.in.RegisterUserUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Endpoint REST para gestión de usuarios.
 */
@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    private RegisterUserUseCase registerUserUseCase;

    @POST
    @Path("/register")
    public Response register(RegisterUserCommand command) {
        UserId userId = registerUserUseCase.register(command);
        return Response.status(Response.Status.CREATED)
                .entity(userId.value())
                .build();
    }
}
