package co.ufps.trainingcenter.infrastructure.security;

import co.ufps.trainingcenter.application.port.out.TokenService;
import co.ufps.trainingcenter.domain.account.UserId;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class JwtTokenService implements TokenService {

    private static final String ISSUER = "trainingcenter";

    private final byte[] secret;
    private final Duration ttl;

    public JwtTokenService(
            @ConfigProperty(name = "trainingcenter.jwt.secret") String secret,
            @ConfigProperty(name = "trainingcenter.jwt.ttl-minutes", defaultValue = "60") long ttlMinutes) {
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            throw new IllegalStateException("trainingcenter.jwt.secret debe tener al menos 32 caracteres");
        }
        this.secret = bytes;
        this.ttl = Duration.ofMinutes(ttlMinutes);
    }

    @Override
    public String issue(UserId userId) {
        try {
            Instant now = Instant.now();
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .issuer(ISSUER)
                    .subject(userId.value())
                    .issueTime(Date.from(now))
                    .expirationTime(Date.from(now.plus(ttl)))
                    .jwtID(UUID.randomUUID().toString())
                    .build();
            SignedJWT jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
            jwt.sign(new MACSigner(secret));
            return jwt.serialize();
        } catch (JOSEException e) {
            throw new IllegalStateException("No se pudo firmar el token", e);
        }
    }

    @Override
    public Optional<UserId> verify(String token) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            if (!JWSAlgorithm.HS256.equals(jwt.getHeader().getAlgorithm())) {
                return Optional.empty();
            }
            if (!jwt.verify(new MACVerifier(secret))) {
                return Optional.empty();
            }
            JWTClaimsSet claims = jwt.getJWTClaimsSet();
            Date expiration = claims.getExpirationTime();
            if (!ISSUER.equals(claims.getIssuer())
                    || expiration == null
                    || expiration.before(new Date())
                    || claims.getSubject() == null
                    || claims.getSubject().isBlank()) {
                return Optional.empty();
            }
            return Optional.of(UserId.of(claims.getSubject()));
        } catch (ParseException | JOSEException | RuntimeException e) {
            return Optional.empty();
        }
    }
}