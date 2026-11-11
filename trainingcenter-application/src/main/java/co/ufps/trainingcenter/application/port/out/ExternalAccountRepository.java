package co.ufps.trainingcenter.application.port.out;

import co.ufps.trainingcenter.domain.account.ExternalAccount;
import co.ufps.trainingcenter.domain.account.UserId;

import java.util.Optional;

public interface ExternalAccountRepository {

    Optional<ExternalAccount> findByUserIdAndPlatform(UserId userId, String platform);

    Optional<ExternalAccount> findByPlatformAndExternalId(String platform, String externalId);

    ExternalAccount save(ExternalAccount account);
}