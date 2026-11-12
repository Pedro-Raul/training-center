package co.ufps.trainingcenter.api.submission;

import co.ufps.trainingcenter.api.security.AuthenticatedUser;
import co.ufps.trainingcenter.domain.account.SubmissionCountByDate;
import co.ufps.trainingcenter.application.port.in.GetSubmissionsByDateUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ForbiddenException;

import co.ufps.trainingcenter.domain.account.UserId;
import jakarta.ws.rs.PathParam;
import java.time.LocalDate;
import java.util.List;

@Path("/api/v1/users/{userId}/submissions/by-date")
@Produces(MediaType.APPLICATION_JSON)
public class UserSubmissionResource {

    @Context
    private SecurityContext securityContext;

    public record SubmissionsByDateResponse(LocalDate date, long total) {

        static SubmissionsByDateResponse from(SubmissionCountByDate count) {
            return new SubmissionsByDateResponse(count.getDate(), count.getTotal());
        }
    }

    private final GetSubmissionsByDateUseCase getSubmissionsByDate;

    @Inject
    public UserSubmissionResource(GetSubmissionsByDateUseCase getSubmissionsByDate) {
        this.getSubmissionsByDate = getSubmissionsByDate;
    }

    @GET
    @AuthenticatedUser
    public List<SubmissionsByDateResponse> getByDate(
            @PathParam("userId") String userId,
            @QueryParam("from") LocalDate from,
            @QueryParam("to") LocalDate to) {

        ensureAuthenticatedUser(userId);
        LocalDate end = to != null ? to : LocalDate.now();
        LocalDate start = from != null ? from : end.minusDays(29);

        return getSubmissionsByDate.getSubmissionsByDate(UserId.of(userId), start, end).stream()
                .map(SubmissionsByDateResponse::from)
                .toList();
    }

    private void ensureAuthenticatedUser(String requestedUserId) {
        if (securityContext == null || securityContext.getUserPrincipal() == null
                || !requestedUserId.equals(securityContext.getUserPrincipal().getName())) {
            throw new ForbiddenException("No puedes consultar los envíos de otro usuario.");
        }
    }
}
