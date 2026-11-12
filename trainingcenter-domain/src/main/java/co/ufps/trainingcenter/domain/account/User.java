package co.ufps.trainingcenter.domain.account;

import co.ufps.trainingcenter.domain.DomainException;
import java.time.LocalDateTime;
import java.util.Objects;

public class User {

    private final UserId id;
    private final String username;
    private final String email;
    private String passwordHash;
    private Role role;
    private final LocalDateTime createdAt;

    private User(UserId id, String username, String email, String passwordHash, Role role) {
        this(id, username, email, passwordHash, role, LocalDateTime.now());
    }

    private User(UserId id, String username, String email, String passwordHash, Role role, LocalDateTime createdAt) {
        this.id = Objects.requireNonNull(id, "id");
        this.username = Objects.requireNonNull(username, "username");
        this.email = Objects.requireNonNull(email, "email");
        this.passwordHash = Objects.requireNonNull(passwordHash, "passwordHash es obligatorio");
        this.role = Objects.requireNonNull(role, "role");
        this.createdAt = Objects.requireNonNullElseGet(createdAt, LocalDateTime::now);
    }

    public static User register(String email, String username, String passwordHash) {
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

    public static User restore(UserId id, String email, String username, String passwordHash, Role role, LocalDateTime createdAt) {
        return new User(id, username, email, passwordHash, role, createdAt);
    }

    public UserId getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public Role getRole() { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

