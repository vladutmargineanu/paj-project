package org.gym.appointment;

import org.gym.exceptions.PremiumAppointmentException;
import org.gym.users.Client;

import java.time.LocalDate;

public class DefaultAppointment extends AbstractAppointment {

    private static final Double PRICE = 50.0;

    public DefaultAppointment(int id, Client client, LocalDate appointmentDate) {
        super(id, PRICE, client, appointmentDate);
    }

    @Override
    public Boolean rescheduleAppointment(LocalDate date) throws PremiumAppointmentException {
        throw new PremiumAppointmentException(this.getId(), "Only PREMIUM appointments can be rescheduled.");
    }

    @Override
    public Boolean cancelAppointment() throws PremiumAppointmentException {
        throw new PremiumAppointmentException(this.getId(), "Only PREMIUM appointments can be canceled.");
    }

    @Override
    public String toString() {
        return "DefaultAppointment{" +
                "id=" + getId() +
                ", price=" + getPrice() +
                ", client=" + getClient() +
                ", appointmentDate=" + getAppointmentDate() +
                '}';
    }

}
