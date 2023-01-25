package org.gym.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
public class Client {
    private String name;
    private LocalDate birthday;
}
