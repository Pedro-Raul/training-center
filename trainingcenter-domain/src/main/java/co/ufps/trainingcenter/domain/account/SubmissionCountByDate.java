package co.ufps.trainingcenter.domain.account;

import java.time.LocalDate;

public class SubmissionCountByDate {
    private final LocalDate date;
    private final long total;

    public SubmissionCountByDate(LocalDate date, long total) {
        this.date = date;
        this.total = total;
    }

    public LocalDate getDate() { return date; }

    public long getTotal() { return total; }
}