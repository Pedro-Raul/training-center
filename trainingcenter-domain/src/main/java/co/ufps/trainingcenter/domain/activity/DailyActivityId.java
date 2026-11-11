package co.ufps.trainingcenter.domain.activity;


import co.ufps.trainingcenter.domain.DomainException;

import java.util.Objects;
import java.util.UUID;

public final class DailyActivityId {
    private final String value;

    private DailyActivityId(String value) {
        if (value == null || value.isBlank())
            throw new DomainException("El dailyActivityId no puede estar vacÃ­o");
        this.value = value.strip();
    }

    public static DailyActivityId generate() {
        return new DailyActivityId(UUID.randomUUID().toString());
    }

    public static DailyActivityId of(String value) {
        return new DailyActivityId(value);
    }

    public String value() {return value;}

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyActivityId that = (DailyActivityId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}

