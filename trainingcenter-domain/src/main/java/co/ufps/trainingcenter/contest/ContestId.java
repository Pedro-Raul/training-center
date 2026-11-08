package co.ufps.trainingcenter.contest;
import co.ufps.trainingcenter.DomainException;

import java.util.Objects;
import java.util.UUID;

public class ContestId {

    private final String value;

    private ContestId (String value){
        if (value == null || value.isBlank()) {
            throw new DomainException("ContestId no puede estar vacío");
        }

            this.value= value.strip();
    }
    public static ContestId generate() {return new ContestId(UUID.randomUUID().toString());}
    public static ContestId of(String value) { return new ContestId(value);}
    public String value () { return  value; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ContestId contestId = (ContestId) o;
        return Objects.equals(value, contestId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
