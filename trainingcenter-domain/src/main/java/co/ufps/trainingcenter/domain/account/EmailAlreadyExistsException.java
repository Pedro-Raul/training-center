package co.ufps.trainingcenter.domain.account;

import co.ufps.trainingcenter.domain.DomainException;

public class EmailAlreadyExistsException extends DomainException {
    public EmailAlreadyExistsException(String email) {
        super("Ya existe una cuenta con ese email: " + email);
    }
}