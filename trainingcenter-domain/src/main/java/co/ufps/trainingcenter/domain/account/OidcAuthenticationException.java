package co.ufps.trainingcenter.domain.account;

import co.ufps.trainingcenter.domain.DomainException;

public class OidcAuthenticationException extends DomainException {
    public OidcAuthenticationException(String message) { super(message); }
}