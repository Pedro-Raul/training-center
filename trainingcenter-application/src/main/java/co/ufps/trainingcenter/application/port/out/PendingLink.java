package co.ufps.trainingcenter.application.port.out;

import co.ufps.trainingcenter.account.UserId;

import java.time.Instant;

public record PendingLink(UserId userId, String nonce, Instant expiresAt) {
}