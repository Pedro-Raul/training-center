package co.ufps.trainingcenter.domain;

/**
 * Excepción lanzada cuando una entidad de dominio no se encuentra.
 * Se mapea a HTTP 404 NOT FOUND.
 */
public class NotFoundException extends DomainException {

    public NotFoundException(String message) {
        super(message);
    }

    public static NotFoundException of(String entityType, String id) {
        return new NotFoundException(entityType + " no encontrado con id: " + id);
    }
}
