package co.ufps.trainingcenter.external.codeforces;

import co.ufps.trainingcenter.application.port.out.OidcStateStore;
import co.ufps.trainingcenter.application.port.out.PendingLink;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class InMemoryOidcStateStore implements OidcStateStore {

    private final ConcurrentHashMap<String, PendingLink> store = new ConcurrentHashMap<>();

    @Override
    public void save(String state, PendingLink pending) {
        Instant now = Instant.now();
        store.values().removeIf(p -> p.expiresAt().isBefore(now));   // limpia los vencidos
        store.put(state, pending);
    }

    @Override
    public Optional<PendingLink> consume(String state) {
        return Optional.ofNullable(store.remove(state));
    }
}