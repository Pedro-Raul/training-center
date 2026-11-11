package co.ufps.trainingcenter.domain.exercise;

import co.ufps.trainingcenter.domain.account.UserSubmissionId;

import java.util.Objects;
import java.util.UUID;

public class ExerciseId {

    private final String value;

    public ExerciseId(String value) {
        if(value == null || value.isBlank())
            throw new IllegalArgumentException("El exerciseId no puede ser nulo o vacio");

        this.value = value;
    }

    public static ExerciseId of(String value) { return new ExerciseId(value);}
    public static ExerciseId generate() {
        return new ExerciseId(UUID.randomUUID().toString());
    }
    public String value() { return value; }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExerciseId that = (ExerciseId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
