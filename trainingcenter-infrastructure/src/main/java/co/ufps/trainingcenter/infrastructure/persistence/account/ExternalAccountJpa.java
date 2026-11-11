package co.ufps.trainingcenter.persistence.account;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "external_accounts")
public class ExternalAccountJpa {

    @Id
    @Column(name = "account_id", length = 36, nullable = false)
    private String accountId;

    @Column(name = "user_id", length = 36, nullable = false)
    private String userId;

    @Column(name = "platform", length = 30, nullable = false)
    private String platform;

    @Column(name = "external_id", length = 100, nullable = false)
    private String externalId;

    @Column(name = "handle", length = 100, nullable = false)
    private String handle;

    @Column(name = "platform_rating", nullable = false)
    private int platformRating;

    @Column(name = "last_synced_at", nullable = false)
    private LocalDateTime lastSyncedAt;

    // Constructor requerido por JPA
    public ExternalAccountJpa() {}

    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }

    public String getExternalId() { return externalId; }
    public void setExternalId(String externalId) { this.externalId = externalId; }

    public String getHandle() { return handle; }
    public void setHandle(String handle) { this.handle = handle; }

    public int getPlatformRating() { return platformRating; }
    public void setPlatformRating(int platformRating) { this.platformRating = platformRating; }

    public LocalDateTime getLastSyncedAt() { return lastSyncedAt; }
    public void setLastSyncedAt(LocalDateTime lastSyncedAt) { this.lastSyncedAt = lastSyncedAt; }
}