package co.ufps.trainingcenter.infrastructure.persistence.account;

import co.ufps.trainingcenter.domain.account.ExternalAccount;
import co.ufps.trainingcenter.domain.account.UserId;

public final class ExternalAccountJpaMapper {

    public ExternalAccountJpa toJpa(ExternalAccount account) {
        ExternalAccountJpa entity = new ExternalAccountJpa();
        entity.setAccountId(account.getAccountId());
        entity.setUserId(account.getUserId().value());
        entity.setPlatform(account.getPlatform());
        entity.setExternalId(account.getExternalId());
        entity.setHandle(account.getHandle());
        entity.setPlatformRating(account.getPlatformRating());
        entity.setLastSyncedAt(account.getLastSyncedAt());
        return entity;
    }

    public ExternalAccount toDomain(ExternalAccountJpa entity) {
        return ExternalAccount.restore(
                entity.getAccountId(),
                UserId.of(entity.getUserId()),
                entity.getPlatform(),
                entity.getExternalId(),
                entity.getHandle(),
                entity.getPlatformRating(),
                entity.getLastSyncedAt()
        );
    }
}