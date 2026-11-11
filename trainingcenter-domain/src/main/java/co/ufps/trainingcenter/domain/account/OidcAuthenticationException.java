package co.ufps.trainingcenter.account;

import co.ufps.trainingcenter.DomainException;

public class OidcAuthenticationException extends DomainException {
    public OidcAuthenticationException(String message) { super(message); }
}