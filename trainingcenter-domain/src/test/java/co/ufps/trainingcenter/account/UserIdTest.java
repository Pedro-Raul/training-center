package co.ufps.trainingcenter.account;

import co.ufps.trainingcenter.domain.DomainException;
import co.ufps.trainingcenter.domain.account.UserId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserIdTest {

    @Test
    void shouldCreateUserIdFromValidString() {
        UserId id = UserId.of("test-uuid-123");

        assertEquals("test-uuid-123", id.value());
    }

    @Test
    void shouldGenerateRandomUserId() {
        UserId id1 = UserId.generate();
        UserId id2 = UserId.generate();

        assertNotNull(id1.value());
        assertNotNull(id2.value());
        assertNotEquals(id1, id2);
    }

    @Test
    void shouldThrowExceptionForNullValue() {
        assertThrows(DomainException.class, () -> UserId.of(null));
    }

    @Test
    void shouldThrowExceptionForBlankValue() {
        assertThrows(DomainException.class, () -> UserId.of("  "));
    }

    @Test
    void shouldTrimValue() {
        UserId id = UserId.of("  test-uuid  ");

        assertEquals("test-uuid", id.value());
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        UserId id1 = UserId.of("same-uuid");
        UserId id2 = UserId.of("same-uuid");
        UserId id3 = UserId.of("different-uuid");

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1, id3);
    }
}
