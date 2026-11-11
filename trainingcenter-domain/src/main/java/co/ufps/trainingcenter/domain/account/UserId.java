package co.ufps.trainingcenter.domain.account;

import co.ufps.trainingcenter.domain.DomainException;

import java.util.Objects;
import java.util.UUID;

public final class UserId{

    private final String value;

    private UserId (String value) {
        if (value == null || value.isBlank()) {
            throw new DomainException("UserId invalido: no puede estar vacio");
        }
        this.value = value.strip();
    }

    public static UserId generate() {
        return new UserId(UUID.randomUUID().toString());
    }

    public static UserId of(String value) {
        return new UserId(value);
    }

    public String value () { return  value; }

    @Override
    public boolean equals(Object o) {
        if(this == o ) return  true;

        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        return value.equals(userId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}