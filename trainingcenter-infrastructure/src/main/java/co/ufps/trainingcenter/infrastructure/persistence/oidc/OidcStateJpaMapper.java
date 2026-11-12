package co.ufps.trainingcenter.infrastructure.persistence.oidc;

import co.ufps.trainingcenter.application.port.out.PendingLink;
import co.ufps.trainingcenter.domain.account.UserId;

public final class OidcStateJpaMapper {

    public OidcStateJpaMapper() {}

    public static OidcStateJpa toJpa(String state, PendingLink pending) {
        OidcStateJpa entity = new OidcStateJpa();
        entity.setState(state);
        entity.setUserId(pending.userId().value());
        entity.setNonce(pending.nonce());
        entity.setExpiresAt(pending.expiresAt());
        return entity;
    }

    public static PendingLink toPendingLink(OidcStateJpa entity) {
        return new PendingLink(
                UserId.of(entity.getUserId()),
                entity.getNonce(),
                entity.getExpiresAt()
        );
    }
}
