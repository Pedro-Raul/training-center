package co.ufps.trainingcenter.application.service;

import co.ufps.trainingcenter.domain.DomainException;
import co.ufps.trainingcenter.application.port.in.SearchContestsUseCase;
import co.ufps.trainingcenter.application.port.out.ContestSearchRepository;
import co.ufps.trainingcenter.application.query.ContestFilter;
import co.ufps.trainingcenter.application.query.PageRequest;
import co.ufps.trainingcenter.application.query.PageResult;
import co.ufps.trainingcenter.domain.contest.Contest;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Objects;

@ApplicationScoped
public class ContestSearchService implements SearchContestsUseCase {

    private final ContestSearchRepository repository;

    public ContestSearchService(ContestSearchRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public PageResult<Contest> search(ContestFilter filter, PageRequest pageRequest) {
        if (filter == null || pageRequest == null) {
            throw new DomainException("El filtro y la paginaciÃ³n son obligatorios");
        }
        return repository.search(filter, pageRequest);
    }
}