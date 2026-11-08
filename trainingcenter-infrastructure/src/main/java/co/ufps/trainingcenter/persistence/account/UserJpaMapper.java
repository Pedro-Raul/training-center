package co.ufps.trainingcenter.persistence.account;

import co.ufps.trainingcenter.account.User;

public final class UserJpaMapper {

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
}