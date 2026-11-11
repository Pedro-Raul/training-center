package co.ufps.trainingcenter.application.port.in;

/**
 * Caso de uso para inicio de sesión de usuarios.
 * Valida credenciales y retorna información del usuario y su perfil.
 */
public interface LoginUserUseCase {

    LoginUserResult login(LoginUserCommand command);
}
