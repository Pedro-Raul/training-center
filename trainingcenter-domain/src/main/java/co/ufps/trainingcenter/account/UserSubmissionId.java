package co.ufps.trainingcenter.account;

import java.util.Objects;
import java.util.UUID;

public class UserSubmissionId {
    private final String value;

    private UserSubmissionId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El UserSubmissionId no puede ser nulo o vacío");
        }
        this.value = value;
    }

    public static UserSubmissionId of(String value) { return new UserSubmissionId(value);}
    public static UserSubmissionId generate() {
        return new UserSubmissionId(UUID.randomUUID().toString());
    }
    public String value() { return value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSubmissionId that = (UserSubmissionId) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}