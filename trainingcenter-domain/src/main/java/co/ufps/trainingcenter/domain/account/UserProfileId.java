package co.ufps.trainingcenter.domain.account;

import co.ufps.trainingcenter.domain.DomainException;
import co.ufps.trainingcenter.domain.activity.DailyActivityId;

import java.util.Objects;
import java.util.UUID;

public final class UserProfileId {
        private final String value;

        private UserProfileId(String value) {
            if (value == null || value.isBlank())
                throw new DomainException("El profileId no puede estar vacÃ­o");
            this.value = value.strip();
        }
    public static UserProfileId generate() {
        return new UserProfileId(UUID.randomUUID().toString());
    }

    public static UserProfileId of(String value) {
        return new UserProfileId(value);
    }

    public String value() {return value;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfileId that = (UserProfileId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
