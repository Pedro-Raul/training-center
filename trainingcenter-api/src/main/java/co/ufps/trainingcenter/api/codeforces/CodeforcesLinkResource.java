package co.ufps.trainingcenter.api.codeforces;

import co.ufps.trainingcenter.account.ExternalAccount;
import co.ufps.trainingcenter.account.UserId;
import co.ufps.trainingcenter.application.port.in.GetCodeforcesAccountUseCase;
import co.ufps.trainingcenter.application.port.in.StartCodeforcesLinkUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.time.LocalDateTime;

@Path("/users/{userId}/codeforces")
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

    @Inject
    public CodeforcesLinkResource(StartCodeforcesLinkUseCase startLink,
                                  GetCodeforcesAccountUseCase getAccount) {
        this.startLink = startLink;
        this.getAccount = getAccount;
    }

    @POST
    @Path("/link")
    public LinkStartResponse start(@PathParam("userId") String userId) {
        return new LinkStartResponse(startLink.startLink(UserId.of(userId)));
    }

    @GET
    public ExternalAccountResponse get(@PathParam("userId") String userId) {
        return getAccount.getByUserId(UserId.of(userId))
                .map(ExternalAccountResponse::from)
                .orElseThrow(NotFoundException::new);
    }
}