package co.ufps.trainingcenter.account;

import co.ufps.trainingcenter.DomainException;

public class ExternalAccountAlreadyLinkedException extends DomainException {
    public ExternalAccountAlreadyLinkedException(String handle) {
        super("La cuenta de Codeforces '" + handle + "' ya está vinculada a otro usuario");
    }
}