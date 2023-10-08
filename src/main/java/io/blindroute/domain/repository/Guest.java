package io.blindroute.domain.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Guest {
    private String Name;
    private Boolean isArrived=false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guest guest = (Guest) o;
        return Objects.equals(getName(), guest.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
