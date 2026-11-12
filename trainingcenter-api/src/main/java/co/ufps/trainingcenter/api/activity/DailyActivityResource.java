package co.ufps.trainingcenter.api.activity;

import co.ufps.trainingcenter.api.security.AuthenticatedUser;

import co.ufps.trainingcenter.domain.account.UserId;
import co.ufps.trainingcenter.domain.activity.DailyActivity;
import co.ufps.trainingcenter.application.port.in.GetDailyActivityMapUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ForbiddenException;

import java.time.LocalDate;
import java.util.List;

@Path("/api/v1/users/{userId}/daily-activity")
@Produces(MediaType.APPLICATION_JSON)
public class DailyActivityResource {

    @Context
    private SecurityContext securityContext;

    public record DailyActivityResponse(LocalDate date, int problemsSolved, int totalSubmissions) {

        static DailyActivityResponse from(DailyActivity activity) {
            return new DailyActivityResponse(
                    activity.getActivityDate(),
                    activity.getProblemsSolved(),
                    activity.getTotalSubmissions());
        }
    }

    private final GetDailyActivityMapUseCase getDailyActivityMap;

    @Inject
    public DailyActivityResource(GetDailyActivityMapUseCase getDailyActivityMap) {
        this.getDailyActivityMap = getDailyActivityMap;
    }

    @GET
    @AuthenticatedUser
    public List<DailyActivityResponse> getMap(
            @PathParam("userId") String userId,
            @QueryParam("from") LocalDate from,
            @QueryParam("to") LocalDate to) {

        ensureAuthenticatedUser(userId);
        LocalDate end = to != null ? to : LocalDate.now();
        LocalDate start = from != null ? from : end.minusDays(364);

        return getDailyActivityMap.getActivityMap(UserId.of(userId), start, end).stream()
                .map(DailyActivityResponse::from)
                .toList();
    }

    private void ensureAuthenticatedUser(String requestedUserId) {
        if (securityContext == null || securityContext.getUserPrincipal() == null
                || !requestedUserId.equals(securityContext.getUserPrincipal().getName())) {
            throw new ForbiddenException("No puedes consultar la actividad de otro usuario.");
        }
    }
}
