package co.ufps.trainingcenter.persistence.account;

import co.ufps.trainingcenter.account.UserId;
import co.ufps.trainingcenter.account.UserProfile;
import co.ufps.trainingcenter.account.UserProfileId;

public final class UserProfileJpaMapper {

    public UserProfileJpa toJpa(UserProfile profile) {
        UserProfileJpa entity = new UserProfileJpa();
        entity.setProfileId(profile.getProfileId().value());
        entity.setUserId(profile.getUserId().value());
        entity.setRating(profile.getRating());
        entity.setCurrentStreak(profile.getCurrentStreak());
        entity.setMaxStreak(profile.getMaxStreak());
        entity.setLastActivityDate(profile.getLastActivityDate());
        entity.setProblemsSolvedCount(profile.getProblemsSolvedCount());
        return entity;
    }

    public UserProfile toDomain(UserProfileJpa entity) {
        return UserProfile.restore(
                UserProfileId.of(entity.getProfileId()),
                UserId.of(entity.getUserId()),
                entity.getRating(),
                entity.getCurrentStreak(),
                entity.getMaxStreak(),
                entity.getLastActivityDate(),
                entity.getProblemsSolvedCount()
        );
    }
}