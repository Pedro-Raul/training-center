package co.ufps.trainingcenter.application.port.out;

public interface PasswordHasher {

    String hash(String rawPassword);
}