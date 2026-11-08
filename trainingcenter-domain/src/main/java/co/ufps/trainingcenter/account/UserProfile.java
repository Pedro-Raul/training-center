package co.ufps.trainingcenter.account;

import co.ufps.trainingcenter.DomainException;

import java.time.LocalDate;
import java.util.Objects;

public class UserProfile {

    private UserProfileId profileId;
    private String userId;
    private int rating ;
    private int currentStreak;
    private int maxStreak ;
    private LocalDate LastActivityDate ;
    private int problemsSolvedCount ;

    private UserProfile(UserProfileId profileId, String userId) {
        this.profileId = Objects.requireNonNull(profileId, "profileId es obligatorio");
        if (userId == null || userId.isBlank())
            throw new DomainException("El nombre del perfil es obligatorio");
        this.userId = userId.strip();
        this.rating = rating;
        this.currentStreak = currentStreak;
        this.maxStreak = maxStreak;
        this.LastActivityDate = LocalDate.now();
        this.problemsSolvedCount = problemsSolvedCount;
    }

    // Getters
    public UserProfileId getProfileId() {return profileId;}
    public int getCurrentStreak() { return currentStreak;}
    public String getUserId() {return userId;}
    public int getRating() { return rating;}
    public int getMaxStreak() {return maxStreak;}
    public LocalDate getLastActivityDate() {return LastActivityDate;}
    public int getProblemsSolvedCount() { return problemsSolvedCount;}
}
