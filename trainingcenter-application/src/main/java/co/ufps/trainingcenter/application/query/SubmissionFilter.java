package co.ufps.trainingcenter.application.query;

import co.ufps.trainingcenter.domain.DomainException;
import co.ufps.trainingcenter.domain.account.Verdict;

import java.time.LocalDate;

public record SubmissionFilter(Verdict verdict, String language, String exerciseId,
                               LocalDate from, LocalDate to) {

    public SubmissionFilter {
        language = blankToNull(language);
        exerciseId = blankToNull(exerciseId);
        if (from != null && to != null && from.isAfter(to)) {
            throw new DomainException("from no puede ser posterior a to");
        }
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.strip();
    }
}