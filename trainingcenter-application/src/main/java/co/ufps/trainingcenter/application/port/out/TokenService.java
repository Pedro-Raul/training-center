package co.ufps.trainingcenter.application.port.out;

import co.ufps.trainingcenter.domain.account.UserId;

import java.util.Optional;

public interface TokenService {
    String issue(UserId userId);
    Optional<UserId> verify(String token);
}