package co.ufps.trainingcenter.application.port.out;

import java.util.Optional;

public interface OidcStateStore {

    void save(String state, PendingLink pending);

    // devuelve el registro y lo elimina: el state solo se puede usar una vez
    Optional<PendingLink> consume(String state);
}
