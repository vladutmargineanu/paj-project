package org.gym.exceptions;

import java.time.LocalDate;

public class NotAvailableDateException extends DateLimitExceededException {

    private static final long serialVersionUID = -3034651278778929257L;

    private LocalDate actualDate;

    public NotAvailableDateException(DateLimitExceededException e, LocalDate actualDate) {
        super(e.getMessage());
        this.id = e.getId();
        this.appointmentDate = e.getAppointmentDate();
        this.actualDate = actualDate;
    }

    public LocalDate getActualDate() {
        return actualDate;
    }
}
