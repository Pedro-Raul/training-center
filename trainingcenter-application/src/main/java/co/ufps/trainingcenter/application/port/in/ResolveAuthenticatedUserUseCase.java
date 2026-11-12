package co.ufps.trainingcenter.application.port.in;

import co.ufps.trainingcenter.domain.account.UserId;

import java.util.Optional;

public interface ResolveAuthenticatedUserUseCase {
    Optional<UserId> authenticate(String bearerToken);
}