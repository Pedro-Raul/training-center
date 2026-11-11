package co.ufps.trainingcenter.domain.account;

import co.ufps.trainingcenter.domain.DomainException;

/**
 * Excepción lanzada cuando las credenciales de inicio de sesión son inválidas.
 */
public class InvalidCredentialsException extends DomainException {

    public InvalidCredentialsException() {
        super("Credenciales inválidas. Verifica tu email y contraseña.");
    }
}
