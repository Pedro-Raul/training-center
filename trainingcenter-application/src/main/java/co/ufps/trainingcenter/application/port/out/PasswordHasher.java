package co.ufps.trainingcenter.application.port.out;

public interface PasswordHasher {

    String hash(String rawPassword);

    /**
     * Verifica si una contraseña en texto plano coincide con un hash almacenado.
     *
     * @param rawPassword la contraseña en texto plano
     * @param hash        el hash almacenado
     * @return true si la contraseña coincide con el hash
     */
    boolean verify(String rawPassword, String hash);
}