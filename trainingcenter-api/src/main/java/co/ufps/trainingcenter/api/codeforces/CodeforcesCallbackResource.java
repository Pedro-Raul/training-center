package co.ufps.trainingcenter.api.codeforces;

import co.ufps.trainingcenter.domain.DomainException;
import co.ufps.trainingcenter.application.port.in.CompleteCodeforcesLinkUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/codeforces/callback")
@Produces(MediaType.APPLICATION_JSON)
public class CodeforcesCallbackResource {

    private final CompleteCodeforcesLinkUseCase completeLink;

    @Inject
    public CodeforcesCallbackResource(CompleteCodeforcesLinkUseCase completeLink) {
        this.completeLink = completeLink;
    }

    @GET
    public CodeforcesLinkResource.ExternalAccountResponse callback(
            @QueryParam("code") String code,
            @QueryParam("state") String state,
            @QueryParam("error") String error) {

        if (error != null) {
            throw new DomainException("Codeforces rechazÃ³ la autorizaciÃ³n: " + error);
        }
        return CodeforcesLinkResource.ExternalAccountResponse.from(
                completeLink.completeLink(code, state));
    }
}