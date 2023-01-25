package org.gym.appointment;

import org.gym.exceptions.DateLimitExceededException;
import org.gym.exceptions.NotAvailableDateException;
import org.gym.exceptions.PremiumAppointmentException;
import org.gym.users.Client;

import java.time.LocalDate;

public class PremiumAppointment extends AbstractAppointment {

    private static final Double PRICE = 75.0;
    private final Client client;

    public PremiumAppointment(int id, Client client, LocalDate appointmentDate) {
        super(id, PRICE, client, appointmentDate);
        this.client = client;
    }

    @Override
    public Client getClient() {
        return client;
    }

    @Override
    public Boolean rescheduleAppointment(LocalDate date) throws PremiumAppointmentException {
        try {
            return super.rescheduleAppointment(date);
        } catch (PremiumAppointmentException e) {
            System.out.println("This is a PREMIUM account. Please retry the operation!");
            throw new PremiumAppointmentException(this.getId(), "Try again!");
        }
    }

    @Override
    public Boolean cancelAppointment() throws NotAvailableDateException, PremiumAppointmentException {
        try {
            return super.cancelAppointment();
        } catch (DateLimitExceededException dateLimitExceededException) {
            throw new NotAvailableDateException(dateLimitExceededException, LocalDate.now());
        } catch (PremiumAppointmentException e) {
            System.out.println("This is a PREMIUM account. Please retry the operation!");
            throw new PremiumAppointmentException(this.getId(), "Try again!");
        }
    }

}
