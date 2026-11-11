package co.ufps.trainingcenter.infrastructure.security;

import at.favre.lib.crypto.bcrypt.BCrypt;
import co.ufps.trainingcenter.application.port.out.PasswordHasher;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class BcryptPasswordHasher implements PasswordHasher {

    private static final int LOG_ROUNDS = 12;

    @Override
    public String hash(String rawPassword) {
        return BCrypt.withDefaults().hashToString(LOG_ROUNDS, rawPassword.toCharArray());
    }

    @Override
    public boolean verify(String rawPassword, String hash) {

        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), hash);
        return result.verified;
    }
}