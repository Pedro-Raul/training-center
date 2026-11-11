package co.ufps.trainingcenter.api.user;

import co.ufps.trainingcenter.domain.account.UserId;
import co.ufps.trainingcenter.application.port.in.GetUserProfileUseCase;
import co.ufps.trainingcenter.application.port.in.LoginUserCommand;
import co.ufps.trainingcenter.application.port.in.LoginUserResult;
import co.ufps.trainingcenter.application.port.in.LoginUserUseCase;
import co.ufps.trainingcenter.application.port.in.RegisterUserCommand;
import co.ufps.trainingcenter.application.port.in.RegisterUserUseCase;
import co.ufps.trainingcenter.application.port.in.UpdateProfileCommand;
import co.ufps.trainingcenter.application.port.in.UpdateProfileUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Endpoint REST para gestiÃ³n de usuarios.
 * Incluye registro, inicio de sesiÃ³n, consulta y actualizaciÃ³n de perfil.
 */
@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    private RegisterUserUseCase registerUserUseCase;

    @Inject
    private LoginUserUseCase loginUseCase;

    @Inject
    private GetUserProfileUseCase getUserProfileUseCase;

    @Inject
    private UpdateProfileUseCase updateProfileUseCase;

    // DTOs

    public record LoginRequest(String email, String password) {
    }

    public record LoginResponse(
            String userId,
            String email,
            String username,
            int rating,
            int currentStreak,
            int maxStreak,
            int problemsSolvedCount
    ) {
        static LoginResponse from(LoginUserResult result) {
            return new LoginResponse(
                    result.userId(),
                    result.email(),
                    result.username(),
                    result.rating(),
                    result.currentStreak(),
                    result.maxStreak(),
                    result.problemsSolvedCount()
            );
        }
    }

    public record ProfileRequest(String username) {
    }

    public record ProfileResponse(
            String userId,
            String email,
            String username,
            int rating,
            int currentStreak,
            int maxStreak,
            int problemsSolvedCount
    ) {
        static ProfileResponse from(LoginUserResult result) {
            return new ProfileResponse(
                    result.userId(),
                    result.email(),
                    result.username(),
                    result.rating(),
                    result.currentStreak(),
                    result.maxStreak(),
                    result.problemsSolvedCount()
            );
        }
    }

    // Endpoints

    @POST
    @Path("/register")
    public Response register(RegisterUserCommand command) {
        UserId userId = registerUserUseCase.register(command);
        return Response.status(Response.Status.CREATED)
                .entity(userId.value())
                .build();
    }

    @POST
    @Path("/login")
    public LoginResponse login(LoginRequest request) {
        LoginUserCommand command = new LoginUserCommand(request.email(), request.password());
        LoginUserResult result = loginUseCase.login(command);
        return LoginResponse.from(result);
    }

    @GET
    @Path("/{userId}/profile")
    public ProfileResponse getProfile(@PathParam("userId") String userId) {
        LoginUserResult result = getUserProfileUseCase.getUserProfile(UserId.of(userId));
        return ProfileResponse.from(result);
    }

    @PUT
    @Path("/{userId}/profile")
    public ProfileResponse updateProfile(@PathParam("userId") String userId, ProfileRequest request) {
        UpdateProfileCommand command = new UpdateProfileCommand(UserId.of(userId), request.username());
        LoginUserResult result = updateProfileUseCase.updateProfile(command);
        return ProfileResponse.from(result);
    }
}
