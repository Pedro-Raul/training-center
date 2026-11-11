package co.ufps.trainingcenter.application.port.in;

import co.ufps.trainingcenter.domain.account.UserId;

/**
 * Caso de uso para obtener el perfil completo de un usuario.
 */
public interface GetUserProfileUseCase {

    LoginUserResult getUserProfile(UserId userId);
}
