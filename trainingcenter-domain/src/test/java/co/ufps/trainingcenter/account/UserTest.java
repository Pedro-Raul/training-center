package co.ufps.trainingcenter.account;

import co.ufps.trainingcenter.domain.DomainException;
import co.ufps.trainingcenter.domain.account.Role;
import co.ufps.trainingcenter.domain.account.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldRegisterUserSuccessfully() {
        User user = User.register("test@example.com", "testuser", "hashed123");

        assertNotNull(user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("testuser", user.getUsername());
        assertEquals("hashed123", user.getPasswordHash());
        assertEquals(Role.USER, user.getRole());
        assertNotNull(user.getCreatedAt());
    }

    @Test
    void shouldThrowExceptionForNullEmail() {
        assertThrows(DomainException.class, () ->
                User.register(null, "testuser", "hashed123")
        );
    }

    @Test
    void shouldThrowExceptionForBlankEmail() {
        assertThrows(DomainException.class, () ->
                User.register("  ", "testuser", "hashed123")
        );
    }

    @Test
    void shouldThrowExceptionForNullPasswordHash() {
        assertThrows(DomainException.class, () ->
                User.register("test@example.com", "testuser", null)
        );
    }

    @Test
    void shouldThrowExceptionForBlankUsername() {
        assertThrows(DomainException.class, () ->
                User.register("test@example.com", "", "hashed123")
        );
    }
}
