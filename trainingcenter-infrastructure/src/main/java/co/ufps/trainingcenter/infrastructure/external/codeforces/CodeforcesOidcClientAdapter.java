package co.ufps.trainingcenter.external.codeforces;

import co.ufps.trainingcenter.DomainException;
import co.ufps.trainingcenter.account.OidcAuthenticationException;
import co.ufps.trainingcenter.application.port.out.CodeforcesIdentity;
import co.ufps.trainingcenter.application.port.out.CodeforcesOidcClient;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.JWKSourceBuilder;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.JSONObjectUtils;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.ConfigProvider;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@ApplicationScoped
public class CodeforcesOidcClientAdapter implements CodeforcesOidcClient {

    private static final Logger LOG = Logger.getLogger(CodeforcesOidcClientAdapter.class.getName());

    private record Discovery(String issuer, String authorizationEndpoint, String tokenEndpoint,
                             DefaultJWTProcessor<SecurityContext> processor) {}

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
            String form = "grant_type=authorization_code"
                    + "&code=" + enc(code)
                    + "&redirect_uri=" + enc(cfg("codeforces.oidc.redirect-uri"))
                    + "&client_id=" + enc(clientId)
                    + "&client_secret=" + enc(cfg("codeforces.oidc.client-secret"));

            Map<String, Object> tokens = sendJson(HttpRequest.newBuilder(URI.create(d.tokenEndpoint()))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(form))
                    .build());

            Object idToken = tokens.get("id_token");
            if (idToken == null) {
                throw new OidcAuthenticationException("La respuesta no incluye id_token");
            }

            JWTClaimsSet claims = d.processor().process(idToken.toString(), null); // firma + exp

            if (!d.issuer().equals(claims.getIssuer()))
                throw new OidcAuthenticationException("Emisor del token inválido");
            if (claims.getAudience() == null || !claims.getAudience().contains(clientId))
                throw new OidcAuthenticationException("Audiencia del token inválida");
            if (!expectedNonce.equals(claims.getStringClaim("nonce")))
                throw new OidcAuthenticationException("Nonce inválido");

            LOG.info("Claims del id_token: " + claims.getClaims().keySet());

            String handle = claims.getStringClaim("handle");      // verificar nombre real del claim
            if (claims.getSubject() == null || handle == null)
                throw new OidcAuthenticationException("El token no trae sub o handle");

            Object r = claims.getClaim("rating");                  // verificar nombre real del claim
            int rating = r instanceof Number n ? Math.max(0, n.intValue()) : 0;

            return new CodeforcesIdentity(claims.getSubject(), handle, rating);
        } catch (DomainException e) {
            throw e;
        } catch (Exception e) {
            throw new OidcAuthenticationException(
                    "No se pudo completar el login con Codeforces: " + e.getMessage());
        }
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

            JWSAlgorithm alg = JWSAlgorithm.RS256;
            if (doc.get("id_token_signing_alg_values_supported") instanceof List<?> algs
                    && !algs.isEmpty() && !algs.contains("RS256")) {
                alg = JWSAlgorithm.parse(algs.get(0).toString());
            }

            JWKSource<SecurityContext> keys = JWKSourceBuilder
                    .create(URI.create(doc.get("jwks_uri").toString()).toURL())
                    .build();
            DefaultJWTProcessor<SecurityContext> processor = new DefaultJWTProcessor<>();
            processor.setJWSKeySelector(new JWSVerificationKeySelector<>(alg, keys));

            return new Discovery(
                    doc.get("issuer").toString(),
                    doc.get("authorization_endpoint").toString(),
                    doc.get("token_endpoint").toString(),
                    processor);
        } catch (DomainException e) {
            throw e;
        } catch (Exception e) {
            throw new OidcAuthenticationException("No se pudo leer la configuración OIDC de Codeforces");
        }
    }

    private Map<String, Object> sendJson(HttpRequest request) throws Exception {
        HttpResponse<String> res = http.send(request, HttpResponse.BodyHandlers.ofString());
        if (res.statusCode() != 200) {
            LOG.warning("Codeforces respondió " + res.statusCode() + ": " + res.body());
            throw new OidcAuthenticationException("Codeforces respondió HTTP " + res.statusCode());
        }
        return JSONObjectUtils.parse(res.body());
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