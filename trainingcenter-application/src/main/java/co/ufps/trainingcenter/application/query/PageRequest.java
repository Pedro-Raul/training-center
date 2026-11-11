package co.ufps.trainingcenter.application.query;

import co.ufps.trainingcenter.domain.DomainException;

public record PageRequest(int page, int size, String sortBy, SortDirection direction) {

    public static final int DEFAULT_SIZE = 20;
    public static final int MAX_SIZE = 100;

    public PageRequest {
        if (page < 1) {
            throw new DomainException("page debe ser mayor o igual a 1");
        }
        if (size < 1 || size > MAX_SIZE) {
            throw new DomainException("size debe estar entre 1 y " + MAX_SIZE);
        }
        if ((long) (page - 1) * size > Integer.MAX_VALUE) {
            throw new DomainException("page es demasiado grande");
        }
        sortBy = (sortBy == null || sortBy.isBlank()) ? null : sortBy.strip();
        direction = direction == null ? SortDirection.DESC : direction;
    }

    public int offset() {
        return (page - 1) * size;
    }
}