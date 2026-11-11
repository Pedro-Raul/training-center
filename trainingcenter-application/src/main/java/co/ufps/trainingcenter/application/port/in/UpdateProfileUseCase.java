package co.ufps.trainingcenter.application.port.in;

/**
 * Caso de uso para actualizar el perfil de un usuario.
 */
public interface UpdateProfileUseCase {

    LoginUserResult updateProfile(UpdateProfileCommand command);
}
