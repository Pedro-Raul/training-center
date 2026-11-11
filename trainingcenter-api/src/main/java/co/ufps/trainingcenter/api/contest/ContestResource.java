package co.ufps.trainingcenter.api.contest;

import co.ufps.trainingcenter.application.port.in.ContestUseCase;
import co.ufps.trainingcenter.domain.contest.Contest;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Endpoint REST para gestiÃ³n de concursos.
 */
@Path("/api/contests")
@Produces(MediaType.APPLICATION_JSON)
public class ContestResource {

    public record ContestResponse(
            String contestId,
            String title,
            String platform,
            String url,
            LocalDateTime startTime,
            Integer durationSeconds,
            String status,
            Boolean isCustom) {

        static ContestResponse from(Contest contest) {
            return new ContestResponse(
                    contest.getId().value(),
                    contest.getTitle(),
                    contest.getPlatform(),
                    contest.getUrl(),
                    contest.getStartTime(),
                    contest.getDurationSeconds(),
                    contest.getStatus().name(),
                    contest.isCustom());
        }
    }

    private final ContestUseCase contestUseCase;

    @Inject
    public ContestResource(ContestUseCase contestUseCase) {
        this.contestUseCase = contestUseCase;
    }

    @GET
    @Path("/active")
    public List<ContestResponse> getActiveContests() {
        return contestUseCase.getActiveContests().stream()
                .map(ContestResponse::from)
                .toList();
    }
}
