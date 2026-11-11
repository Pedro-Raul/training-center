package co.ufps.trainingcenter.domain.account;

import co.ufps.trainingcenter.domain.DomainException;

public class ExternalAccountAlreadyLinkedException extends DomainException {
    public ExternalAccountAlreadyLinkedException(String handle) {
        super("La cuenta de Codeforces '" + handle + "' ya estÃ¡ vinculada a otro usuario");
    }
}