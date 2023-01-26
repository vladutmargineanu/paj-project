package org.gym.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
public class Trainer {
    private String name;
    private LocalDate birthday;

    @Override
    public String toString() {
        return "Trainer{" +
                "name='" + name + '\'' +
                ", birthday=" + birthday +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trainer trainer)) return false;

        if (getName() != null ? !getName().equals(trainer.getName()) : trainer.getName() != null) return false;
        return getBirthday() != null ? getBirthday().equals(trainer.getBirthday()) : trainer.getBirthday() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getBirthday() != null ? getBirthday().hashCode() : 0);
        return result;
    }
}
