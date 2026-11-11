package co.ufps.trainingcenter.application.port.in;

import co.ufps.trainingcenter.domain.account.ExternalAccount;
import co.ufps.trainingcenter.domain.account.UserId;

import java.util.Optional;

public interface GetCodeforcesAccountUseCase {

    Optional<ExternalAccount> getByUserId(UserId userId);
}