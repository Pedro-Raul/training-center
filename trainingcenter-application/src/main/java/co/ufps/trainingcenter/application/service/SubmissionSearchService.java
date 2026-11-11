package co.ufps.trainingcenter.application.service;

import co.ufps.trainingcenter.DomainException;
import co.ufps.trainingcenter.account.UserId;
import co.ufps.trainingcenter.account.UserSubmission;
import co.ufps.trainingcenter.application.port.in.SearchSubmissionsUseCase;
import co.ufps.trainingcenter.application.port.out.UserSubmissionSearchRepository;
import co.ufps.trainingcenter.application.query.PageRequest;
import co.ufps.trainingcenter.application.query.PageResult;
import co.ufps.trainingcenter.application.query.SubmissionFilter;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Objects;

@ApplicationScoped
public class SubmissionSearchService implements SearchSubmissionsUseCase {

    private final UserSubmissionSearchRepository repository;

    public SubmissionSearchService(UserSubmissionSearchRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public PageResult<UserSubmission> search(UserId userId, SubmissionFilter filter, PageRequest pageRequest) {
        if (userId == null) {
            throw new DomainException("El usuario es obligatorio");
        }
        if (filter == null || pageRequest == null) {
            throw new DomainException("El filtro y la paginación son obligatorios");
        }
        return repository.search(userId, filter, pageRequest);
    }
}