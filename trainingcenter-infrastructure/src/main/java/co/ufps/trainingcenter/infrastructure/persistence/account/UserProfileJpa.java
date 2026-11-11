
package co.ufps.trainingcenter.infrastructure.persistence.account;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "user_profiles")
public class UserProfileJpa {

    @Id
    @Column(name = "profile_id", length = 36, nullable = false)
    private String profileId;

    @Column(name = "user_id", length = 36, nullable = false)
    private String userId;

    @Column(name = "rating", nullable = false)
    private int rating;

    @Column(name = "current_streak", nullable = false)
    private int currentStreak;

    @Column(name = "max_streak", nullable = false)
    private int maxStreak;

    @Column(name = "last_activity_date")
    private LocalDate lastActivityDate;

    @Column(name = "problems_solved_count", nullable = false)
    private int problemsSolvedCount;

    // Constructor requerido por JPA
    public UserProfileJpa() {}

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getMaxStreak() {
        return maxStreak;
    }

    public void setMaxStreak(int maxStreak) {
        this.maxStreak = maxStreak;
    }

    public LocalDate getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(LocalDate lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    public int getProblemsSolvedCount() {
        return problemsSolvedCount;
    }

    public void setProblemsSolvedCount(int problemsSolvedCount) {
        this.problemsSolvedCount = problemsSolvedCount;
    }
}
