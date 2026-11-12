package co.ufps.trainingcenter.domain.activity;

import co.ufps.trainingcenter.domain.DomainException;
import co.ufps.trainingcenter.domain.account.UserId;

import java.time.LocalDate;
import java.util.Objects;

public class DailyActivity {

    private DailyActivityId activityId;
    private UserId userId;
    private LocalDate activityDate ;
    private int problemsSolved;
    private int totalSubmissions ;

    private DailyActivity(DailyActivityId activityId, UserId userId, LocalDate activityDate, int problemsSolved, int totalSubmissions) {
        this.activityId = Objects.requireNonNull(activityId, "activityId es obligatorio");
        this.userId = Objects.requireNonNull(userId, "userId es obligatorio");
        this.activityDate = Objects.requireNonNull(activityDate, "activityDate es obligatorio");

        if (problemsSolved < 0) {
            throw new DomainException("problemsSolved no puede ser negativo");
        }
        if (totalSubmissions < 0) {
            throw new DomainException("totalSubmissions no puede ser negativo");
        }
        if (problemsSolved > totalSubmissions) {
            throw new DomainException("problemsSolved no puede superar totalSubmissions");
        }
        this.problemsSolved = problemsSolved;
        this.totalSubmissions = totalSubmissions;
    }

    public static DailyActivity register(UserId userId, LocalDate activityDate, int problemsSolved, int totalSubmissions) {
        return new DailyActivity(DailyActivityId.generate(), userId, activityDate, problemsSolved, totalSubmissions);
    }

    public static DailyActivity restore(DailyActivityId activityId, UserId userId, LocalDate activityDate, int problemsSolved, int totalSubmissions) {
        return new DailyActivity(activityId, userId, activityDate, problemsSolved, totalSubmissions);
    }

    public static DailyActivity startOf(UserId userId, LocalDate activityDate) {
        return register(userId, activityDate, 0, 0);
    }

    public void recordSubmission(boolean solved) {
        totalSubmissions++;
        if (solved) {
            problemsSolved++;
        }
    }

    public DailyActivityId getActivityId() {return activityId;}
    public UserId getUserId() {return userId;}
    public LocalDate getActivityDate() {return activityDate;}
    public int getProblemsSolved() {return problemsSolved;}
    public int getTotalSubmissions() {return totalSubmissions;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyActivity that = (DailyActivity) o;
        return Objects.equals(activityId, that.activityId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(activityId);
    }
}
