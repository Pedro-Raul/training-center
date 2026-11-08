package co.ufps.trainingcenter.account;

import co.ufps.trainingcenter.DomainException;

import java.time.LocalDateTime;
import java.util.Objects;

public class ExternalAccount {
 private String accountId;
 private UserId userId;
 private String platform;
 private String handle;
 private int platformRating;
 private LocalDateTime lastSyncedAt ;

 private ExternalAccount(String profileId, UserId userId, String platform, String handle) {
        if (accountId == null || accountId.isBlank())
            throw new DomainException("el accountId es obligatorio");
        this.accountId = accountId;
        this.userId = Objects.requireNonNull(userId, "userId es obligatorio");
        if (platform == null || platform.isBlank())
         throw new DomainException("La plataforma es obligatoria");
        this.platform = platform.strip();;
        if (handle == null || handle.isBlank())
         throw new DomainException("el handle es obligatorio");
        this.handle = handle.strip();;
        this.platformRating = platformRating;
        this.lastSyncedAt = LocalDateTime.now();
    }
    public String getAccountId() {return accountId;}
    public UserId getUserId() { return userId;}
    public String getPlatform() {return platform;}
    public String getHandle() {return handle;}
    public int getPlatformRating() { return platformRating;}
    public LocalDateTime getLastSyncedAt() { return lastSyncedAt;}
}
