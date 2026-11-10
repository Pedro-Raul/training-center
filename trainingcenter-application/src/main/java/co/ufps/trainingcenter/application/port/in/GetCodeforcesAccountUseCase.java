package co.ufps.trainingcenter.application.port.in;

import co.ufps.trainingcenter.account.ExternalAccount;
import co.ufps.trainingcenter.account.UserId;

import java.util.Optional;

public interface GetCodeforcesAccountUseCase {

    Optional<ExternalAccount> getByUserId(UserId userId);
}