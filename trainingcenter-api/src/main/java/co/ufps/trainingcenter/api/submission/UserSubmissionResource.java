package co.ufps.trainingcenter.api.submission;

import co.ufps.trainingcenter.account.SubmissionCountByDate;
import co.ufps.trainingcenter.application.port.in.GetSubmissionsByDateUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import co.ufps.trainingcenter.account.UserId;
import jakarta.ws.rs.PathParam;
import java.time.LocalDate;
import java.util.List;

@Path("/users/{userId}/submissions/by-date")
@Produces(MediaType.APPLICATION_JSON)
public class UserSubmissionResource {

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
    public List<SubmissionsByDateResponse> getByDate(
            @PathParam("userId") String userId,
            @QueryParam("from") LocalDate from,
            @QueryParam("to") LocalDate to) {

        LocalDate end = to != null ? to : LocalDate.now();
        LocalDate start = from != null ? from : end.minusDays(29);

        return getSubmissionsByDate.getSubmissionsByDate(UserId.of(userId), start, end).stream()
                .map(SubmissionsByDateResponse::from)
                .toList();
    }
}