package co.ufps.trainingcenter.application.service;

import co.ufps.trainingcenter.application.port.in.IssueAccessTokenUseCase;
import co.ufps.trainingcenter.application.port.in.ResolveAuthenticatedUserUseCase;
import co.ufps.trainingcenter.application.port.out.TokenService;
import co.ufps.trainingcenter.domain.DomainException;
import co.ufps.trainingcenter.domain.account.UserId;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
public class AuthenticationService implements IssueAccessTokenUseCase, ResolveAuthenticatedUserUseCase {

    private final TokenService tokenService;

    public AuthenticationService(TokenService tokenService) {
        this.tokenService = Objects.requireNonNull(tokenService);
    }

    @Override
    public String issueAccessToken(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new DomainException("El usuario es obligatorio");
        }
        return tokenService.issue(UserId.of(userId));
    }

    @Override
    public Optional<UserId> authenticate(String bearerToken) {
        if (bearerToken == null || bearerToken.isBlank()) {
            return Optional.empty();
        }
        return tokenService.verify(bearerToken.strip());
    }
}