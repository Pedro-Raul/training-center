package co.ufps.trainingcenter.domain;

public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}