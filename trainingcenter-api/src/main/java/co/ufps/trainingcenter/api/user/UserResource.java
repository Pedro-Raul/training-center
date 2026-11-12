package co.ufps.trainingcenter.api.user;

import co.ufps.trainingcenter.api.rate.RateLimit;
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
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
 * Endpoint REST para gestion de usuarios.
 * Incluye registro, inicio de sesion, consulta y actualizacion de perfil.
 */
@Path("/api/v1/users")
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

    public record LoginRequest(
            @NotBlank(message = "El email es obligatorio")
            @Email(message = "El email debe tener un formato válido")
            String email,
            @NotBlank(message = "La contraseña es obligatoria")
            String password
    ) {
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

    public record ProfileRequest(
            @NotBlank(message = "El nombre de usuario es obligatorio")
            @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
            String username
    ) {
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
    @RateLimit(maxRequests = 5, windowSeconds = 60)
    public Response register(@Valid RegisterUserCommand command) {
        UserId userId = registerUserUseCase.register(command);
        return Response.status(Response.Status.CREATED)
                .entity(userId.value())
                .build();
    }

    @POST
    @Path("/login")
    @RateLimit(maxRequests = 10, windowSeconds = 60)
    public LoginResponse login(@Valid LoginRequest request) {
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
    public ProfileResponse updateProfile(@PathParam("userId") String userId, @Valid ProfileRequest request) {
        UpdateProfileCommand command = new UpdateProfileCommand(UserId.of(userId), request.username());
        LoginUserResult result = updateProfileUseCase.updateProfile(command);
        return ProfileResponse.from(result);
    }
}
