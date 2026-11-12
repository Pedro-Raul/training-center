package co.ufps.trainingcenter.api.security;

import jakarta.ws.rs.NameBinding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marca los endpoints que exigen un token válido.
 * El filtro AuthenticationFilter pone el userId en el SecurityContext.
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface AuthenticatedUser {
}