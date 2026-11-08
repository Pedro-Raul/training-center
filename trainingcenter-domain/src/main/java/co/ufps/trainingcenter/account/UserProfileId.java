package co.ufps.trainingcenter.account;

import co.ufps.trainingcenter.DomainException;

import java.util.Objects;

public final class UserProfileId {
        private final String value;

        private UserProfileId(String value) {
            if (value == null || value.isBlank())
                throw new DomainException("El profileId no puede estar vacío");
            this.value = value.strip();
        }

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
