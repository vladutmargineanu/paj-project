package org.gym.appointment;

import org.gym.exceptions.DateLimitExceededException;
import org.gym.exceptions.NotAvailableDateException;
import org.gym.exceptions.PremiumAppointmentException;
import org.gym.users.Client;
import org.gym.utils.Status;

import java.time.LocalDate;

public class DefaultAppointment extends AbstractAppointment {

    private static final Double PRICE = 50.0;
    private final Client client;

    public DefaultAppointment(Client client, LocalDate appointmentDate) {
        super(PRICE, client, appointmentDate);
        this.client = client;
    }

    @Override
    public Client getClient() {
        return client;
    }

    @Override
    public Boolean rescheduleAppointment(LocalDate date) {
        System.out.println(Status.INVALID_OPERATION);
        return false;
    }

    @Override
    public Boolean cancelAppointment() throws PremiumAppointmentException {
        throw new PremiumAppointmentException(this.getId(), "Only PREMIUM appointments can be cancels");
    }
}
