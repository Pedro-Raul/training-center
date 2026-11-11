package co.ufps.trainingcenter.infrastructure.security;

import at.favre.lib.crypto.bcrypt.BCrypt;
import co.ufps.trainingcenter.application.port.out.PasswordHasher;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Adaptador de hashing de contraseÃ±as usando BCrypt.
 */
@ApplicationScoped
public class BcryptPasswordHasher implements PasswordHasher {

    private static final int LOG_ROUNDS = 12;

    @Override
    public String hash(String rawPassword) {
        return BCrypt.withDefaults().hashToString(LOG_ROUNDS, rawPassword.toCharArray());
    }
}
