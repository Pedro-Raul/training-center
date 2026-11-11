package co.ufps.trainingcenter.domain.account;

import co.ufps.trainingcenter.domain.DomainException;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class ExternalAccount {
    private final String accountId;
    private final UserId userId;
    private final String platform;
    private String externalId;
    private String handle;
    private int platformRating;
    private LocalDateTime lastSyncedAt;

    private ExternalAccount(String accountId, UserId userId, String platform, String externalId,
                            String handle, int platformRating, LocalDateTime lastSyncedAt) {
        if (accountId == null || accountId.isBlank())
            throw new DomainException("el accountId es obligatorio");
        this.accountId = accountId;
        this.userId = Objects.requireNonNull(userId, "userId es obligatorio");
        if (platform == null || platform.isBlank())
            throw new DomainException("La plataforma es obligatoria");
        this.platform = platform.strip();
        if (externalId == null || externalId.isBlank())
            throw new DomainException("el externalId es obligatorio");
        this.externalId = externalId.strip();
        if (handle == null || handle.isBlank())
            throw new DomainException("el handle es obligatorio");
        this.handle = handle.strip();
        if (platformRating < 0)
            throw new DomainException("platformRating no puede ser negativo");
        this.platformRating = platformRating;
        this.lastSyncedAt = Objects.requireNonNull(lastSyncedAt, "lastSyncedAt es obligatorio");
    }

    public static ExternalAccount link(UserId userId, String platform, String externalId,
                                       String handle, int platformRating) {
        return new ExternalAccount(UUID.randomUUID().toString(), userId, platform, externalId,
                handle, platformRating, LocalDateTime.now());
    }

    public static ExternalAccount restore(String accountId, UserId userId, String platform,
                                          String externalId, String handle, int platformRating,
                                          LocalDateTime lastSyncedAt) {
        return new ExternalAccount(accountId, userId, platform, externalId, handle,
                platformRating, lastSyncedAt);
    }

    public void refresh(String externalId, String handle, int platformRating) {
        if (externalId == null || externalId.isBlank())
            throw new DomainException("el externalId es obligatorio");
        if (handle == null || handle.isBlank())
            throw new DomainException("el handle es obligatorio");
        if (platformRating < 0)
            throw new DomainException("platformRating no puede ser negativo");
        this.externalId = externalId.strip();
        this.handle = handle.strip();
        this.platformRating = platformRating;
        this.lastSyncedAt = LocalDateTime.now();
    }

    public String getAccountId() { return accountId; }
    public UserId getUserId() { return userId; }
    public String getPlatform() { return platform; }
    public String getExternalId() { return externalId; }
    public String getHandle() { return handle; }
    public int getPlatformRating() { return platformRating; }
    public LocalDateTime getLastSyncedAt() { return lastSyncedAt; }
}