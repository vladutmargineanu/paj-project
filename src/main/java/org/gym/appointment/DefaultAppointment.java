package org.gym.appointment;

import org.gym.exceptions.PremiumAppointmentException;
import org.gym.users.Client;

import java.time.LocalDate;

public class DefaultAppointment extends AbstractAppointment {

    private static final Double PRICE = 50.0;
    private final Client client;

    public DefaultAppointment(int id, Client client, LocalDate appointmentDate) {
        super(id, PRICE, client, appointmentDate);
        this.client = client;
    }

    @Override
    public Client getClient() {
        return client;
    }

    @Override
    public Boolean rescheduleAppointment(LocalDate date) throws PremiumAppointmentException {
        throw new PremiumAppointmentException(this.getId(), "Only PREMIUM appointments can be rescheduled.");
    }

    @Override
    public Boolean cancelAppointment() throws PremiumAppointmentException {
        throw new PremiumAppointmentException(this.getId(), "Only PREMIUM appointments can be canceled.");
    }
}
