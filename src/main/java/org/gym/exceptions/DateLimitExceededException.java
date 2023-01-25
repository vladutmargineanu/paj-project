package org.gym.exceptions;

import java.time.LocalDate;

public class DateLimitExceededException extends GymException {

    private static final long serialVersionUID = -3034651278778929257L;

    // Default access specifier - access just in the same package
    // Protected - access to same package and also to different package subclasses
    int id;

    LocalDate appointmentDate;

    public DateLimitExceededException(String message) {
        super(message);
    }

    public DateLimitExceededException(int id, LocalDate appointmentDate, String message) {
        super(message);
        this.id = id;
        this.appointmentDate = appointmentDate;
    }

    public int getId() {
        return id;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }
}
