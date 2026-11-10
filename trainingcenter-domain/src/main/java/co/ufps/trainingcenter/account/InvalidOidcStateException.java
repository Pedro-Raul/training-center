package co.ufps.trainingcenter.account;

import co.ufps.trainingcenter.DomainException;

public class InvalidOidcStateException extends DomainException {
    public InvalidOidcStateException() {
        super("La solicitud de vinculación es inválida o expiró. Inicia el proceso de nuevo");
    }
}