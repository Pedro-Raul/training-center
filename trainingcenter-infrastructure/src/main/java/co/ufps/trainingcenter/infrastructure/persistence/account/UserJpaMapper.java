package co.ufps.trainingcenter.infrastructure.persistence.account;
import co.ufps.trainingcenter.domain.account.*;

import co.ufps.trainingcenter.domain.account.User;
import co.ufps.trainingcenter.infrastructure.persistence.account.UserJpa;

public final class UserJpaMapper {

    // User: Domain -> JPA

    public UserJpa toJpa(User user) {
        UserJpa entity = new UserJpa();
        entity.setUserId(user.getId().value());
        entity.setUsername(user.getUsername());
        entity.setEmail(user.getEmail());
        entity.setPasswordHash(user.getPasswordHash());
        entity.setRole(user.getRole().name());
        entity.setCreatedAt(user.getCreatedAt());
        return entity;
    }

    // User: JPA -> Domain

    public User toDomain(UserJpa entity) {
        return User.restore(
                UserId.of(entity.getUserId()),
                entity.getEmail(),
                entity.getUsername(),
                entity.getPasswordHash(),
                Role.valueOf(entity.getRole()),
                entity.getCreatedAt()
        );
    }

    // UserProfile: Domain -> JPA

    public UserProfileJpa toProfileJpa(UserProfile profile) {
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
