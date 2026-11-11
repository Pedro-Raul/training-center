package co.ufps.trainingcenter.account;

import co.ufps.trainingcenter.DomainException;

public class EmailAlreadyExistsException extends DomainException {
    public EmailAlreadyExistsException(String email) {
        super("Ya existe una cuenta con ese email: " + email);
    }
}