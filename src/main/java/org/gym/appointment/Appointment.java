package org.gym.appointment;

import org.gym.exceptions.DateLimitExceededException;
import org.gym.exceptions.PremiumAppointmentException;
import org.gym.users.Client;

import java.time.LocalDate;

public interface Appointment {

    int getId();
    Double getPrice();

    Client getClient();

    LocalDate getAppointmentDate();

    Boolean rescheduleAppointment(LocalDate date);

    Boolean cancelAppointment() throws DateLimitExceededException, PremiumAppointmentException;
}
