package org.gym.exceptions;

import java.time.LocalDate;

public class NotAvailableDateException extends DateLimitExceededException {

	private static final long serialVersionUID = -3034651278778929257L;

	private LocalDate newAppointmentDate;


	public NotAvailableDateException(DateLimitExceededException e, LocalDate newAppointmentDate) {
        super(e.getMessage());
        this.id = e.getId();
		this.appointmentDate = e.getAppointmentDate();
		this.newAppointmentDate = newAppointmentDate;
    }

	public LocalDate getNewAppointmentDate() {
		return newAppointmentDate;
	}
}
