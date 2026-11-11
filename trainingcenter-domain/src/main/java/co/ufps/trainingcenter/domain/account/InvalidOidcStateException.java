package co.ufps.trainingcenter.domain.account;

import co.ufps.trainingcenter.domain.DomainException;

public class InvalidOidcStateException extends DomainException {
    public InvalidOidcStateException() {
        super("La solicitud de vinculaciÃ³n es invÃ¡lida o expirÃ³. Inicia el proceso de nuevo");
    }
}