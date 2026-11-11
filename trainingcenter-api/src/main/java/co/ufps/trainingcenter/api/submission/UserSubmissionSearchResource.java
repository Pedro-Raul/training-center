package co.ufps.trainingcenter.api.submission;

import co.ufps.trainingcenter.account.UserId;
import co.ufps.trainingcenter.account.UserSubmission;
import co.ufps.trainingcenter.account.Verdict;
import co.ufps.trainingcenter.api.common.PageResponse;
import co.ufps.trainingcenter.api.common.QueryParams;
import co.ufps.trainingcenter.application.port.in.SearchSubmissionsUseCase;
import co.ufps.trainingcenter.application.query.SubmissionFilter;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Path("/users/{userId}/submissions")
@Produces(MediaType.APPLICATION_JSON)
public class UserSubmissionSearchResource {

    public record SubmissionResponse(String id, String exerciseId, Verdict verdict, String language,
                                     int executionTimeMs, LocalDateTime submittedAt) {

        static SubmissionResponse from(UserSubmission s) {
            return new SubmissionResponse(s.getId().value(), s.getExerciseId().value(),
                    s.getVerdict(), s.getLanguage(), s.getExecutionTimeMs(), s.getSubmittedAt());
        }
    }

    private final SearchSubmissionsUseCase searchSubmissions;

    @Inject
    public UserSubmissionSearchResource(SearchSubmissionsUseCase searchSubmissions) {
        this.searchSubmissions = searchSubmissions;
    }

    @GET
    public PageResponse<SubmissionResponse> search(
            @PathParam("userId") String userId,
            @QueryParam("verdict") String verdict,
            @QueryParam("language") String language,
            @QueryParam("exerciseId") String exerciseId,
            @QueryParam("from") LocalDate from,
            @QueryParam("to") LocalDate to,
            @QueryParam("page") Integer page,
            @QueryParam("size") Integer size,
            @QueryParam("sort") String sort,
            @QueryParam("direction") String direction) {

        SubmissionFilter filter = new SubmissionFilter(
                QueryParams.parseEnum(Verdict.class, "verdict", verdict),
                language, exerciseId, from, to);

        return PageResponse.from(
                searchSubmissions.search(UserId.of(userId), filter,
                                QueryParams.pageRequest(page, size, sort, direction))
                        .map(SubmissionResponse::from));
    }
}