package co.ufps.trainingcenter.infrastructure.external.codeforces;

import co.ufps.trainingcenter.domain.DomainException;
import co.ufps.trainingcenter.domain.account.OidcAuthenticationException;
import co.ufps.trainingcenter.application.port.out.CodeforcesIdentity;
import co.ufps.trainingcenter.application.port.out.CodeforcesOidcClient;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.util.JSONObjectUtils;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.ConfigProvider;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
/**
 * Cliente OIDC de Codeforces. El id_token se firma con HS256 usando el
 * client_secret como llave (el discovery no publica jwks_uri).
 */
@ApplicationScoped
public class CodeforcesOidcClientAdapter implements CodeforcesOidcClient {

    private static final Logger LOG = Logger.getLogger(CodeforcesOidcClientAdapter.class.getName());

    private record Discovery(String issuer, String authorizationEndpoint, String tokenEndpoint) {}

    private final HttpClient http = HttpClient.newHttpClient();
    private volatile Discovery discovery;

    @Override
    public String buildAuthorizationUrl(String state, String nonce) {
        Discovery d = discovery();
        return d.authorizationEndpoint() + "?response_type=code"
                + "&client_id=" + enc(cfg("codeforces.oidc.client-id"))
                + "&redirect_uri=" + enc(cfg("codeforces.oidc.redirect-uri"))
                + "&scope=openid"
                + "&state=" + enc(state)
                + "&nonce=" + enc(nonce);
    }

    @Override
    public CodeforcesIdentity exchangeCode(String code, String expectedNonce) {
        try {
            Discovery d = discovery();
            String clientId = cfg("codeforces.oidc.client-id");
            String clientSecret = cfg("codeforces.oidc.client-secret");
            String form = "grant_type=authorization_code"
                    + "&code=" + enc(code)
                    + "&redirect_uri=" + enc(cfg("codeforces.oidc.redirect-uri"))
                    + "&client_id=" + enc(clientId)
                    + "&client_secret=" + enc(clientSecret);

            Map<String, Object> tokens = sendJson(HttpRequest.newBuilder(URI.create(d.tokenEndpoint()))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(form))
                    .build());

            Object idToken = tokens.get("id_token");
            if (idToken == null) {
                throw new OidcAuthenticationException("La respuesta no incluye id_token");
            }

            JWTClaimsSet claims = verify(idToken.toString(), clientSecret);

            if (!d.issuer().equals(claims.getIssuer()))
                throw new OidcAuthenticationException("Emisor del token invÃ¡lido");
            if (claims.getAudience() == null || !claims.getAudience().contains(clientId))
                throw new OidcAuthenticationException("Audiencia del token invÃ¡lida");
            Date exp = claims.getExpirationTime();
            if (exp == null || exp.before(new Date()))
                throw new OidcAuthenticationException("El token expirÃ³");

            // El nonce es opcional: solo se valida si Codeforces lo incluye en el token.
            String tokenNonce = claims.getStringClaim("nonce");
            if (tokenNonce != null && !tokenNonce.equals(expectedNonce))
                throw new OidcAuthenticationException("Nonce invÃ¡lido");

            LOG.info("Claims del id_token: " + claims.getClaims().keySet());

            String handle = claims.getStringClaim("handle");
            if (claims.getSubject() == null || handle == null)
                throw new OidcAuthenticationException("El token no trae sub o handle");

            Object r = claims.getClaim("rating");
            int rating = r instanceof Number n ? Math.max(0, n.intValue()) : 0;

            return new CodeforcesIdentity(claims.getSubject(), handle, rating);
        } catch (DomainException e) {
            throw e;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Fallo en el intercambio OIDC con Codeforces", e);
            throw new OidcAuthenticationException(
                    "No se pudo completar el login con Codeforces: " + e);
        }
    }

    private JWTClaimsSet verify(String idToken, String clientSecret) throws Exception {
        SignedJWT jwt = SignedJWT.parse(idToken);
        if (!JWSAlgorithm.HS256.equals(jwt.getHeader().getAlgorithm())) {
            throw new OidcAuthenticationException(
                    "Algoritmo de firma no soportado: " + jwt.getHeader().getAlgorithm());
        }
        if (!jwt.verify(new MACVerifier(clientSecret.getBytes(StandardCharsets.UTF_8)))) {
            throw new OidcAuthenticationException("La firma del id_token es invÃ¡lida");
        }
        return jwt.getJWTClaimsSet();
    }

    private Discovery discovery() {
        Discovery d = discovery;
        if (d != null) return d;
        synchronized (this) {
            if (discovery == null) discovery = fetchDiscovery();
            return discovery;
        }
    }

    private Discovery fetchDiscovery() {
        try {
            Map<String, Object> doc = sendJson(
                    HttpRequest.newBuilder(URI.create(cfg("codeforces.oidc.discovery-url"))).GET().build());
            return new Discovery(
                    required(doc, "issuer"),
                    required(doc, "authorization_endpoint"),
                    required(doc, "token_endpoint"));
        } catch (DomainException e) {
            throw e;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Fallo leyendo el discovery OIDC", e);
            throw new OidcAuthenticationException(
                    "No se pudo leer la configuraciÃ³n OIDC de Codeforces: " + e);
        }
    }

    private Map<String, Object> sendJson(HttpRequest request) throws Exception {
        HttpResponse<String> res = http.send(request, HttpResponse.BodyHandlers.ofString());
        if (res.statusCode() != 200) {
            LOG.warning("Codeforces respondiÃ³ " + res.statusCode() + ": " + res.body());
            throw new OidcAuthenticationException("Codeforces respondiÃ³ HTTP " + res.statusCode());
        }
        return JSONObjectUtils.parse(res.body());
    }

    private static String required(Map<String, Object> doc, String key) {
        Object value = doc.get(key);
        if (value == null || value.toString().isBlank()) {
            throw new OidcAuthenticationException("El discovery de Codeforces no incluye " + key);
        }
        return value.toString();
    }

    private static String cfg(String key) {
        return ConfigProvider.getConfig().getOptionalValue(key, String.class)
                .filter(s -> !s.isBlank())
                .orElseThrow(() -> new OidcAuthenticationException("Falta configurar " + key));
    }

    private static String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}