package co.ufps.trainingcenter.application.query;

import co.ufps.trainingcenter.DomainException;
import co.ufps.trainingcenter.contest.Status;

import java.time.LocalDate;

public record ContestFilter(Status status, String platform, Boolean custom, String title,
                            LocalDate from, LocalDate to) {

    public ContestFilter {
        platform = blankToNull(platform);
        title = blankToNull(title);
        if (from != null && to != null && from.isAfter(to)) {
            throw new DomainException("from no puede ser posterior a to");
        }
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.strip();
    }
}