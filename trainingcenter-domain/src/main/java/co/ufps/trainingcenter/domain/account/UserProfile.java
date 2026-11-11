package co.ufps.trainingcenter.account;

import co.ufps.trainingcenter.DomainException;

import java.time.LocalDate;
import java.util.Objects;

public class UserProfile {

    private UserProfileId profileId;
    private UserId userId;
    private int rating ;
    private int currentStreak;
    private int maxStreak ;
    private LocalDate lastActivityDate ;
    private int problemsSolvedCount ;

    private UserProfile(UserProfileId profileId, UserId userId , int rating, int currentStreak, int maxStreak, LocalDate lastActivityDate, int problemsSolvedCount)  {
        this.profileId = Objects.requireNonNull(profileId, "profileId es obligatorio");
        this.userId = Objects.requireNonNull(userId, "userId es obligatorio");

        if (rating < 0)throw new DomainException("rating no puede ser negativo");
        if (currentStreak < 0) throw new DomainException("currentStreak no puede ser negativo");
        if (maxStreak < currentStreak)throw new DomainException("maxStreak no puede ser menor que currentStreak");
        if (problemsSolvedCount < 0) throw new DomainException("problemsSolvedCount no puede ser negativo");

        this.rating = rating;
        this.currentStreak =currentStreak;
        this.maxStreak = maxStreak;
        this.lastActivityDate = lastActivityDate;
        this.problemsSolvedCount = problemsSolvedCount;
    }

    public static UserProfile generate(UserId userId){
        return new UserProfile(UserProfileId.generate(),userId, 0,0,0, null,0);
    }

    public static UserProfile restore(UserProfileId profileId, UserId userId , int rating, int currentStreak, int maxStreak, LocalDate lastActivityDate, int problemsSolvedCount){
        return new UserProfile(profileId,userId,rating,currentStreak,maxStreak,lastActivityDate,problemsSolvedCount);
    }

    // Getters
    public UserProfileId getProfileId() {return profileId;}
    public int getCurrentStreak() { return currentStreak;}
    public UserId getUserId() {return userId;}
    public int getRating() { return rating;}
    public int getMaxStreak() {return maxStreak;}
    public LocalDate getLastActivityDate() {return lastActivityDate;}
    public int getProblemsSolvedCount() { return problemsSolvedCount;}

    public void recordSolvedActivity(LocalDate activityDate) {
        Objects.requireNonNull(activityDate, "activityDate es obligatoria");

        if (lastActivityDate != null && activityDate.isBefore(lastActivityDate)) {
            throw new DomainException(
                    "No se puede registrar actividad anterior a la última fecha registrada"
            );
        }

        if (activityDate.equals(lastActivityDate)) {
            return;
        }

        if (lastActivityDate == null
                || !activityDate.equals(lastActivityDate.plusDays(1))) {
            currentStreak = 1;
        } else {
            currentStreak++;
        }

        maxStreak = Math.max(maxStreak, currentStreak);
        lastActivityDate = activityDate;
    }
}
