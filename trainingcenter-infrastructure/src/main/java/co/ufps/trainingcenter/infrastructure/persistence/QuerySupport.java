package co.ufps.trainingcenter.infrastructure.persistence;

import co.ufps.trainingcenter.domain.DomainException;
import co.ufps.trainingcenter.application.query.PageRequest;
import jakarta.persistence.TypedQuery;

import java.util.Map;

public final class QuerySupport {

    private QuerySupport() {
    }

    public static <T> TypedQuery<T> bind(TypedQuery<T> query, Map<String, Object> params) {
        params.forEach((name, value) -> query.setParameter(name, value));
        return query;
    }

    /** Solo acepta columnas de la lista blanca; el orden secundario hace estable la paginación. */
    public static String orderBy(Map<String, String> sortable, String defaultKey,
                                 PageRequest page, String tieBreaker) {
        String key = page.sortBy() == null ? defaultKey : page.sortBy();
        String column = sortable.get(key);
        if (column == null) {
            throw new DomainException("Campo de orden no soportado: '" + key
                    + "'. Permitidos: " + sortable.keySet().stream().sorted().toList());
        }
        return " ORDER BY " + column + " " + page.direction().name() + ", " + tieBreaker + " ASC";
    }
}