package co.ufps.trainingcenter.application.service;

import co.ufps.trainingcenter.DomainException;
import co.ufps.trainingcenter.account.ExternalAccount;
import co.ufps.trainingcenter.account.ExternalAccountAlreadyLinkedException;
import co.ufps.trainingcenter.account.InvalidOidcStateException;
import co.ufps.trainingcenter.account.UserId;
import co.ufps.trainingcenter.application.port.in.CompleteCodeforcesLinkUseCase;
import co.ufps.trainingcenter.application.port.in.GetCodeforcesAccountUseCase;
import co.ufps.trainingcenter.application.port.in.StartCodeforcesLinkUseCase;
import co.ufps.trainingcenter.application.port.out.CodeforcesIdentity;
import co.ufps.trainingcenter.application.port.out.CodeforcesOidcClient;
import co.ufps.trainingcenter.application.port.out.ExternalAccountRepository;
import co.ufps.trainingcenter.application.port.out.OidcStateStore;
import co.ufps.trainingcenter.application.port.out.PendingLink;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

public class CodeforcesLinkService implements StartCodeforcesLinkUseCase,
        CompleteCodeforcesLinkUseCase, GetCodeforcesAccountUseCase {

    public static final String PLATFORM = "CODEFORCES";
    private static final Duration STATE_TTL = Duration.ofMinutes(10);
    private static final SecureRandom RANDOM = new SecureRandom();

    private final ExternalAccountRepository accountRepository;
    private final CodeforcesOidcClient oidcClient;
    private final OidcStateStore stateStore;

    public CodeforcesLinkService(ExternalAccountRepository accountRepository,
                                 CodeforcesOidcClient oidcClient,
                                 OidcStateStore stateStore) {
        this.accountRepository = Objects.requireNonNull(accountRepository);
        this.oidcClient = Objects.requireNonNull(oidcClient);
        this.stateStore = Objects.requireNonNull(stateStore);
    }

    @Override
    public String startLink(UserId userId) {
        if (userId == null) {
            throw new DomainException("El usuario es obligatorio");
        }
        String state = randomToken();
        String nonce = randomToken();
        stateStore.save(state, new PendingLink(userId, nonce, Instant.now().plus(STATE_TTL)));
        return oidcClient.buildAuthorizationUrl(state, nonce);
    }

    @Override
    public ExternalAccount completeLink(String code, String state) {
        if (code == null || code.isBlank() || state == null || state.isBlank()) {
            throw new DomainException("code y state son obligatorios");
        }

        PendingLink pending = stateStore.consume(state)
                .filter(p -> p.expiresAt().isAfter(Instant.now()))
                .orElseThrow(InvalidOidcStateException::new);

        CodeforcesIdentity identity = oidcClient.exchangeCode(code, pending.nonce());

        accountRepository.findByPlatformAndExternalId(PLATFORM, identity.subject())
                .filter(a -> !a.getUserId().equals(pending.userId()))
                .ifPresent(a -> {
                    throw new ExternalAccountAlreadyLinkedException(identity.handle());
                });

        ExternalAccount account = accountRepository
                .findByUserIdAndPlatform(pending.userId(), PLATFORM)
                .map(existing -> {
                    existing.refresh(identity.subject(), identity.handle(), identity.rating());
                    return existing;
                })
                .orElseGet(() -> ExternalAccount.link(pending.userId(), PLATFORM,
                        identity.subject(), identity.handle(), identity.rating()));

        return accountRepository.save(account);
    }

    @Override
    public Optional<ExternalAccount> getByUserId(UserId userId) {
        return accountRepository.findByUserIdAndPlatform(userId, PLATFORM);
    }

    private static String randomToken() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}