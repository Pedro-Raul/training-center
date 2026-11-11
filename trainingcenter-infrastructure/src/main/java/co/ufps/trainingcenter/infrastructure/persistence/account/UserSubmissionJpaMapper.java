package co.ufps.trainingcenter.infrastructure.persistence.account;

import co.ufps.trainingcenter.domain.account.UserId;
import co.ufps.trainingcenter.domain.account.UserSubmission;
import co.ufps.trainingcenter.domain.account.UserSubmissionId;
import co.ufps.trainingcenter.domain.exercise.ExerciseId;

public class UserSubmissionJpaMapper {

    public static UserSubmission toDomain(UserSubmissionJpa jpa) {
        if (jpa == null) return null;

        return new UserSubmission(
                UserSubmissionId.of(jpa.getSubmissionId()),

                UserId.of(jpa.getUserId()),
                ExerciseId.of(jpa.getExerciseId()),

                jpa.getVerdict(),
                jpa.getLanguage(),
                jpa.getExecutionTimeMs(),
                jpa.getSubmittedAt()
        );
    }

    public static UserSubmissionJpa toJpa(UserSubmission domain) {
        if (domain == null) return null;

        UserSubmissionJpa jpa = new UserSubmissionJpa();

        jpa.setSubmissionId(domain.getId().value());

        jpa.setUserId(domain.getUserId().value());
        jpa.setExerciseId(domain.getExerciseId().value());

        jpa.setVerdict(domain.getVerdict());
        jpa.setLanguage(domain.getLanguage());
        jpa.setExecutionTimeMs(domain.getExecutionTimeMs());
        jpa.setSubmittedAt(domain.getSubmittedAt());

        return jpa;
    }
}
