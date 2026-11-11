package co.ufps.trainingcenter.infrastructure.persistence.activity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
        name = "daily_activity",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_daily_activity_user_date",
                        columnNames = {"user_id", "activity_date"}
                )
        }
)
public class DailyActivityJpa {

    @Id
    @Column(name = "activity_id", length = 36, nullable = false)
    private String activityId;

    @Column(name = "user_id", length = 36, nullable = false)
    private String userId;

    @Column(name = "activity_date", nullable = false)
    private LocalDate activityDate;

    @Column(name = "problems_solved", nullable = false)
    private int problemsSolved;

    @Column(name = "total_submissions", nullable = false)
    private int totalSubmissions;

    // Constructor requerido por JPA
    public DailyActivityJpa() {}

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDate getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(LocalDate activityDate) {
        this.activityDate = activityDate;
    }

    public int getProblemsSolved() {
        return problemsSolved;
    }

    public void setProblemsSolved(int problemsSolved) {
        this.problemsSolved = problemsSolved;
    }

    public int getTotalSubmissions() {
        return totalSubmissions;
    }

    public void setTotalSubmissions(int totalSubmissions) {
        this.totalSubmissions = totalSubmissions;
    }
}