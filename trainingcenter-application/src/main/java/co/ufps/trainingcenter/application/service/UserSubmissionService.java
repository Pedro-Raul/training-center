package co.ufps.trainingcenter.application.service;

import co.ufps.trainingcenter.DomainException;
import co.ufps.trainingcenter.account.SubmissionCountByDate;
import co.ufps.trainingcenter.application.port.in.GetSubmissionsByDateUseCase;
import co.ufps.trainingcenter.application.port.out.UserSubmissionRepository;
import co.ufps.trainingcenter.account.UserId;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class UserSubmissionService implements GetSubmissionsByDateUseCase {

    private final UserSubmissionRepository userSubmissionRepository;

    public UserSubmissionService(UserSubmissionRepository userSubmissionRepository) {
        this.userSubmissionRepository = Objects.requireNonNull(userSubmissionRepository);
    }

    @Override
    public List<SubmissionCountByDate> getSubmissionsByDate(UserId userId, LocalDate from, LocalDate to) {
        if (userId == null) {
            throw new DomainException("El usuario es obligatorio");
        }
        if (from == null || to == null) {
            throw new DomainException("El rango de fechas es obligatorio");
        }
        if (from.isAfter(to)) {
            throw new DomainException("from no puede ser posterior a to");
        }
        return userSubmissionRepository.countByUserIdAndDateBetween(
                userId,
                from.atStartOfDay(),
                to.plusDays(1).atStartOfDay());
    }
}