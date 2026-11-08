package co.ufps.trainingcenter.account;

import co.ufps.trainingcenter.DomainException;
import java.time.LocalDateTime;
import java.util.Objects;

public class User {

    private final UserId id;
    private final String username;
    private final String email;
    private String passwordHash;
    private Role role;
    private final LocalDateTime createdAt;

    private User(UserId id, String username, String email, String passwordHash,Role role) {
        this.id = Objects.requireNonNull(id, "id");
        this.username = Objects.requireNonNull(username, "username");
        this.email = Objects.requireNonNull(email, "email");
        this.passwordHash = Objects.requireNonNull(passwordHash, "passwordHash es obligatorio");
        this.role = Objects.requireNonNull(role, "role");
        this.createdAt = LocalDateTime.now();
    }

    public static User register(String email, String username, String passwordHash, String initialProfileName) {
        if (email == null || email.isBlank()) {
            throw new DomainException("El email es obligatorio");
        }
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new DomainException("La contraseña es obligatoria");
        }
        if (username == null || username.isBlank()) {
            throw new DomainException("El nombre del perfil es obligatorio");
        }

        User user = new User(UserId.generate(),username, email, passwordHash, Role.USER);
        return user;
    }

    public UserId getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public Role getRole() { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}

