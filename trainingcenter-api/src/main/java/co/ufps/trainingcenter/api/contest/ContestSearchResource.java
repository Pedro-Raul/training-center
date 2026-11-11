package co.ufps.trainingcenter.api.contest;

import co.ufps.trainingcenter.api.common.PageResponse;
import co.ufps.trainingcenter.api.common.QueryParams;
import co.ufps.trainingcenter.application.port.in.SearchContestsUseCase;
import co.ufps.trainingcenter.application.query.ContestFilter;
import co.ufps.trainingcenter.domain.contest.Status;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.time.LocalDate;

/**
 * BÃºsqueda de concursos con filtros y paginaciÃ³n.
 */
@Path("/api/contests/search")
@Produces(MediaType.APPLICATION_JSON)
public class ContestSearchResource {

    private final SearchContestsUseCase searchContests;

    @Inject
    public ContestSearchResource(SearchContestsUseCase searchContests) {
        this.searchContests = searchContests;
    }

    @GET
    public PageResponse<ContestResource.ContestResponse> search(
            @QueryParam("status") String status,
            @QueryParam("platform") String platform,
            @QueryParam("custom") Boolean custom,
            @QueryParam("title") String title,
            @QueryParam("from") LocalDate from,
            @QueryParam("to") LocalDate to,
            @QueryParam("page") Integer page,
            @QueryParam("size") Integer size,
            @QueryParam("sort") String sort,
            @QueryParam("direction") String direction) {

        ContestFilter filter = new ContestFilter(
                QueryParams.parseEnum(Status.class, "status", status),
                platform, custom, title, from, to);

        return PageResponse.from(
                searchContests.search(filter, QueryParams.pageRequest(page, size, sort, direction))
                        .map(ContestResource.ContestResponse::from));
    }
}