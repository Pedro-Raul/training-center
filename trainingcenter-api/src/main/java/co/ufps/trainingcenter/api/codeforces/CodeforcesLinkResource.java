package co.ufps.trainingcenter.api.codeforces;

import co.ufps.trainingcenter.account.ExternalAccount;
import co.ufps.trainingcenter.account.UserId;
import co.ufps.trainingcenter.application.port.in.CompleteCodeforcesLinkUseCase;
import co.ufps.trainingcenter.application.port.in.GetCodeforcesAccountUseCase;
import co.ufps.trainingcenter.application.port.in.StartCodeforcesLinkUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;
import java.util.Optional;

/**
 * Endpoint REST para vincular cuentas de Codeforces vía OIDC.
 */
@Path("/api/codeforces")
@Produces(MediaType.APPLICATION_JSON)
public class CodeforcesLinkResource {

    public record CodeforcesAccountResponse(
            String userId,
            String platform,
            String externalId,
            String handle,
            Integer rating) {

        static CodeforcesAccountResponse from(ExternalAccount account) {
            return new CodeforcesAccountResponse(
                    account.getUserId().value(),
                    account.getPlatform(),
                    account.getExternalId(),
                    account.getHandle(),
                    account.getPlatformRating());
        }
    }

    private final StartCodeforcesLinkUseCase startLink;
    private final CompleteCodeforcesLinkUseCase completeLink;
    private final GetCodeforcesAccountUseCase getCodeforcesAccount;

    @Inject
    public CodeforcesLinkResource(StartCodeforcesLinkUseCase startLink,
                                  CompleteCodeforcesLinkUseCase completeLink,
                                  GetCodeforcesAccountUseCase getCodeforcesAccount) {
        this.startLink = startLink;
        this.completeLink = completeLink;
        this.getCodeforcesAccount = getCodeforcesAccount;
    }

    @POST
    @Path("/link/{userId}")
    public Response startLink(@PathParam("userId") String userId) {
        String authorizationUrl = startLink.startLink(UserId.of(userId));
        return Response.ok(Map.of("authorizationUrl", authorizationUrl)).build();
    }

    @GET
    @Path("/callback")
    public Response callback(@QueryParam("code") String code,
                             @QueryParam("state") String state) {
        ExternalAccount account = completeLink.completeLink(code, state);
        return Response.ok(CodeforcesAccountResponse.from(account)).build();
    }

    @GET
    @Path("/account/{userId}")
    public Response getAccount(@PathParam("userId") String userId) {
        Optional<ExternalAccount> account = getCodeforcesAccount.getByUserId(UserId.of(userId));
        if (account.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "No se encontró cuenta de Codeforces para este usuario"))
                    .build();
        }
        return Response.ok(CodeforcesAccountResponse.from(account.get())).build();
    }
}
