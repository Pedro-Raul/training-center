package co.ufps.trainingcenter.api.codeforces;

import co.ufps.trainingcenter.domain.account.ExternalAccount;
import co.ufps.trainingcenter.api.security.AuthenticatedUser;
import co.ufps.trainingcenter.domain.account.UserId;
import co.ufps.trainingcenter.application.port.in.GetCodeforcesAccountUseCase;
import co.ufps.trainingcenter.application.port.in.StartCodeforcesLinkUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import co.ufps.trainingcenter.domain.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ForbiddenException;

import java.time.LocalDateTime;

@Path("/api/v1/users/{userId}/codeforces")
@Produces(MediaType.APPLICATION_JSON)
public class CodeforcesLinkResource {

    public record LinkStartResponse(String authorizationUrl) {
    }

    public record ExternalAccountResponse(String platform, String handle, int rating,
                                          LocalDateTime lastSyncedAt) {

        static ExternalAccountResponse from(ExternalAccount account) {
            return new ExternalAccountResponse(
                    account.getPlatform(),
                    account.getHandle(),
                    account.getPlatformRating(),
                    account.getLastSyncedAt());
        }
    }

    private final StartCodeforcesLinkUseCase startLink;
    private final GetCodeforcesAccountUseCase getAccount;

    @Context
    private SecurityContext securityContext;

    @Inject
    public CodeforcesLinkResource(StartCodeforcesLinkUseCase startLink,
                                  GetCodeforcesAccountUseCase getAccount) {
        this.startLink = startLink;
        this.getAccount = getAccount;
    }

    @POST
    @Path("/link")
    @AuthenticatedUser
    public LinkStartResponse start(@PathParam("userId") String userId) {
        ensureAuthenticatedUser(userId);
        return new LinkStartResponse(startLink.startLink(UserId.of(userId)));
    }

    @GET
    @AuthenticatedUser
    public ExternalAccountResponse get(@PathParam("userId") String userId) {
        ensureAuthenticatedUser(userId);
        return getAccount.getByUserId(UserId.of(userId))
                .map(ExternalAccountResponse::from)
                .orElseThrow(() -> NotFoundException.of("Cuenta Codeforces", userId));
    }

    private void ensureAuthenticatedUser(String requestedUserId) {
        if (securityContext == null || securityContext.getUserPrincipal() == null
                || !requestedUserId.equals(securityContext.getUserPrincipal().getName())) {
            throw new ForbiddenException("No puedes operar sobre la cuenta de otro usuario.");
        }
    }
}