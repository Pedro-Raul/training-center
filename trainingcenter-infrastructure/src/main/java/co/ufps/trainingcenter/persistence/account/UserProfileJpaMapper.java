package co.ufps.trainingcenter.persistence.account;

import co.ufps.trainingcenter.account.UserProfile;

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
}