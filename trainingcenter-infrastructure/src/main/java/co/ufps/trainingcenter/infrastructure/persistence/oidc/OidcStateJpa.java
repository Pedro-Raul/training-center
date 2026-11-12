package co.ufps.trainingcenter.infrastructure.persistence.oidc;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "oidc_state")
public class OidcStateJpa {

    @Id
    @Column(length = 255, nullable = false)
    private String state;

    @Column(name = "user_id", length = 36)
    private String userId;

    @Column(length = 255)
    private String nonce;

    @Column(name = "code_verifier", length = 255)
    private String codeVerifier;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    // Constructor requerido por JPA
    public OidcStateJpa() {}

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getNonce() { return nonce; }
    public void setNonce(String nonce) { this.nonce = nonce; }

    public String getCodeVerifier() { return codeVerifier; }
    public void setCodeVerifier(String codeVerifier) { this.codeVerifier = codeVerifier; }

    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
}
