package co.ufps.trainingcenter.application.service;

import co.ufps.trainingcenter.domain.account.ExternalAccount;
import co.ufps.trainingcenter.domain.account.ExternalAccountAlreadyLinkedException;
import co.ufps.trainingcenter.domain.account.InvalidOidcStateException;
import co.ufps.trainingcenter.domain.account.UserId;
import co.ufps.trainingcenter.application.port.out.CodeforcesIdentity;
import co.ufps.trainingcenter.application.port.out.CodeforcesOidcClient;
import co.ufps.trainingcenter.application.port.out.ExternalAccountRepository;
import co.ufps.trainingcenter.application.port.out.OidcStateStore;
import co.ufps.trainingcenter.application.port.out.PendingLink;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CodeforcesLinkServiceTest {

    private static final UserId USER = UserId.of("user-1");
    private static final String PLATFORM = CodeforcesLinkService.PLATFORM;

    @Mock
    private ExternalAccountRepository accountRepository;

    @Mock
    private CodeforcesOidcClient oidcClient;

    @Mock
    private OidcStateStore stateStore;

    @InjectMocks
    private CodeforcesLinkService service;

    @Test
    void startLinkShouldStoreStateAndReturnAuthorizationUrl() {
        when(oidcClient.buildAuthorizationUrl(anyString(), anyString()))
                .thenReturn("https://codeforces.com/authorize");

        String url = service.startLink(USER);

        assertEquals("https://codeforces.com/authorize", url);
        verify(stateStore).save(anyString(), any(PendingLink.class));
    }

    @Test
    void completeLinkShouldFailWhenStateIsUnknown() {
        when(stateStore.consume("bad")).thenReturn(Optional.empty());

        assertThrows(InvalidOidcStateException.class, () -> service.completeLink("code", "bad"));
        verifyNoInteractions(oidcClient);
    }

    @Test
    void completeLinkShouldCreateAccountWhenNoneExists() {
        PendingLink pending = new PendingLink(USER, "nonce", Instant.now().plusSeconds(60));
        when(stateStore.consume("st")).thenReturn(Optional.of(pending));
        when(oidcClient.exchangeCode("code", "nonce"))
                .thenReturn(new CodeforcesIdentity("sub-1", "tourist", 3800));
        when(accountRepository.findByPlatformAndExternalId(PLATFORM, "sub-1")).thenReturn(Optional.empty());
        when(accountRepository.findByUserIdAndPlatform(USER, PLATFORM)).thenReturn(Optional.empty());
        when(accountRepository.save(any(ExternalAccount.class))).thenAnswer(i -> i.getArgument(0));

        ExternalAccount result = service.completeLink("code", "st");

        assertEquals("tourist", result.getHandle());
        assertEquals("sub-1", result.getExternalId());
        assertEquals("user-1", result.getUserId().value());
    }

    @Test
    void completeLinkShouldFailWhenCodeforcesAccountBelongsToAnotherUser() {
        PendingLink pending = new PendingLink(USER, "nonce", Instant.now().plusSeconds(60));
        ExternalAccount other = ExternalAccount.link(UserId.of("user-2"), PLATFORM, "sub-1", "tourist", 3800);
        when(stateStore.consume("st")).thenReturn(Optional.of(pending));
        when(oidcClient.exchangeCode("code", "nonce"))
                .thenReturn(new CodeforcesIdentity("sub-1", "tourist", 3800));
        when(accountRepository.findByPlatformAndExternalId(PLATFORM, "sub-1")).thenReturn(Optional.of(other));

        assertThrows(ExternalAccountAlreadyLinkedException.class, () -> service.completeLink("code", "st"));
        verify(accountRepository, never()).save(any());
    }
}