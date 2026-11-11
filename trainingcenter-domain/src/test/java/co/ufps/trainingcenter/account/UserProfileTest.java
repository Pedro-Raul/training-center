package co.ufps.trainingcenter.account;

import co.ufps.trainingcenter.domain.DomainException;
import co.ufps.trainingcenter.domain.account.UserId;
import co.ufps.trainingcenter.domain.account.UserProfile;
import co.ufps.trainingcenter.domain.account.UserProfileId;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserProfileTest {

    private final UserId userId = UserId.of("user-123");

    @Test
    void shouldGenerateUserProfileWithDefaultValues() {
        UserProfile profile = UserProfile.generate(userId);

        assertEquals(userId, profile.getUserId());
        assertEquals(0, profile.getRating());
        assertEquals(0, profile.getCurrentStreak());
        assertEquals(0, profile.getMaxStreak());
        assertNull(profile.getLastActivityDate());
        assertEquals(0, profile.getProblemsSolvedCount());
    }

    @Test
    void shouldRecordFirstSolvedActivity() {
        UserProfile profile = UserProfile.generate(userId);
        LocalDate today = LocalDate.of(2026, 10, 11);

        profile.recordSolvedActivity(today);

        assertEquals(1, profile.getCurrentStreak());
        assertEquals(1, profile.getMaxStreak());
        assertEquals(today, profile.getLastActivityDate());
    }

    @Test
    void shouldIncrementStreakForConsecutiveDays() {
        UserProfile profile = UserProfile.generate(userId);

        profile.recordSolvedActivity(LocalDate.of(2026, 10, 10));
        profile.recordSolvedActivity(LocalDate.of(2026, 10, 11));
        profile.recordSolvedActivity(LocalDate.of(2026, 10, 12));

        assertEquals(3, profile.getCurrentStreak());
        assertEquals(3, profile.getMaxStreak());
    }

    @Test
    void shouldResetStreakForNonConsecutiveDay() {
        UserProfile profile = UserProfile.generate(userId);

        profile.recordSolvedActivity(LocalDate.of(2026, 10, 10));
        profile.recordSolvedActivity(LocalDate.of(2026, 10, 12));

        assertEquals(1, profile.getCurrentStreak());
        assertEquals(1, profile.getMaxStreak());
    }

    @Test
    void shouldNotUpdateStreakForSameDay() {
        UserProfile profile = UserProfile.generate(userId);
        LocalDate today = LocalDate.of(2026, 10, 11);

        profile.recordSolvedActivity(today);
        profile.recordSolvedActivity(today);

        assertEquals(1, profile.getCurrentStreak());
    }

    @Test
    void shouldThrowExceptionForPastActivity() {
        UserProfile profile = UserProfile.generate(userId);
        profile.recordSolvedActivity(LocalDate.of(2026, 10, 11));

        assertThrows(DomainException.class, () ->
                profile.recordSolvedActivity(LocalDate.of(2026, 10, 10))
        );
    }

    @Test
    void shouldThrowExceptionForNegativeRating() {
        assertThrows(DomainException.class, () ->
                UserProfile.restore(
                        UserProfileId.of("p1"),
                        userId,
                        -1,
                        0,
                        0,
                        null,
                        0
                )
        );
    }
}
