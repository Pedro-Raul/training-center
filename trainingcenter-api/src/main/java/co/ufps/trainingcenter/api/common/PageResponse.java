package co.ufps.trainingcenter.api.common;

import co.ufps.trainingcenter.application.query.PageResult;

import java.util.List;

public record PageResponse<T>(List<T> items, int page, int size, long totalElements,
                              int totalPages, boolean hasNext, boolean hasPrevious) {

    public static <T> PageResponse<T> from(PageResult<T> result) {
        return new PageResponse<>(result.items(), result.page(), result.size(),
                result.totalElements(), result.totalPages(),
                result.page() < result.totalPages(), result.page() > 1);
    }
}