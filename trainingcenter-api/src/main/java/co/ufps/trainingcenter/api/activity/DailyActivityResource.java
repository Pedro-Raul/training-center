package co.ufps.trainingcenter.api.activity;

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

import java.time.LocalDate;
import java.util.List;

@Path("/api/v1/users/{userId}/daily-activity")
@Produces(MediaType.APPLICATION_JSON)
public class DailyActivityResource {

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
    public List<DailyActivityResponse> getMap(
            @PathParam("userId") String userId,
            @QueryParam("from") LocalDate from,
            @QueryParam("to") LocalDate to) {

        LocalDate end = to != null ? to : LocalDate.now();
        LocalDate start = from != null ? from : end.minusDays(364);

        return getDailyActivityMap.getActivityMap(UserId.of(userId), start, end).stream()
                .map(DailyActivityResponse::from)
                .toList();
    }
}