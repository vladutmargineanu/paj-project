package org.gym.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
public class Client {
    private String name;
    private LocalDate birthday;

    @Override
    public String toString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", birthday=" + birthday +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client client)) return false;
        return Objects.equals(getName(), client.getName()) && Objects.equals(getBirthday(), client.getBirthday());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getBirthday());
    }
}
