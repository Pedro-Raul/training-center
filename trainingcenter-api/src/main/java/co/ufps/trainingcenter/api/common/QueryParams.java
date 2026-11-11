package co.ufps.trainingcenter.api.common;

import co.ufps.trainingcenter.DomainException;
import co.ufps.trainingcenter.application.query.PageRequest;
import co.ufps.trainingcenter.application.query.SortDirection;

import java.util.Arrays;
import java.util.Locale;

public final class QueryParams {

    private QueryParams() {
    }

    public static PageRequest pageRequest(Integer page, Integer size, String sort, String direction) {
        return new PageRequest(
                page == null ? 1 : page,
                size == null ? PageRequest.DEFAULT_SIZE : size,
                sort,
                parseEnum(SortDirection.class, "direction", direction));
    }

    public static <E extends Enum<E>> E parseEnum(Class<E> type, String field, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Enum.valueOf(type, value.strip().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new DomainException("Valor inválido para '" + field + "': " + value
                    + ". Permitidos: " + Arrays.toString(type.getEnumConstants()));
        }
    }
}