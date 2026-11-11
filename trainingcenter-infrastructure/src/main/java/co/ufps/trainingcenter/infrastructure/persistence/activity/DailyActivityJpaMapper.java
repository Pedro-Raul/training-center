package co.ufps.trainingcenter.infrastructure.persistence.activity;

import co.ufps.trainingcenter.domain.account.UserId;
import co.ufps.trainingcenter.domain.activity.DailyActivity;
import co.ufps.trainingcenter.domain.activity.DailyActivityId;

public final class DailyActivityJpaMapper {

    public DailyActivityJpa toJpa(DailyActivity activity) {
        DailyActivityJpa entity = new DailyActivityJpa();
        entity.setActivityId(activity.getActivityId().value());
        entity.setUserId(activity.getUserId().value());
        entity.setActivityDate(activity.getActivityDate());
        entity.setProblemsSolved(activity.getProblemsSolved());
        entity.setTotalSubmissions(activity.getTotalSubmissions());
        return entity;
    }

    public DailyActivity toDomain(DailyActivityJpa entity) {
        return DailyActivity.restore(
                DailyActivityId.of(entity.getActivityId()),
                UserId.of(entity.getUserId()),
                entity.getActivityDate(),
                entity.getProblemsSolved(),
                entity.getTotalSubmissions()
        );
    }
}