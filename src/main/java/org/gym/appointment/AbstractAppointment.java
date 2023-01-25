package org.gym.appointment;

import lombok.AccessLevel;
import lombok.Setter;
import org.gym.exceptions.DateLimitExceededException;
import org.gym.exceptions.PremiumAppointmentException;
import org.gym.users.Client;

import java.time.LocalDate;

@Setter(AccessLevel.PROTECTED)
public abstract class AbstractAppointment implements Appointment {

    private int id;
    private Double price;
    private Client client;
    private LocalDate appointmentDate;

    public AbstractAppointment(int id, Double price, Client client, LocalDate appointmentDate) {
        this.id = id;
        this.price = price;
        this.client = client;
        this.appointmentDate = appointmentDate;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Double getPrice() {
        return price;
    }

    @Override
    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    @Override
    public Client getClient() {
        return client;
    }

    @Override
    public Boolean rescheduleAppointment(LocalDate date) throws PremiumAppointmentException {
        if (this.getId() < 0) {
            throw new PremiumAppointmentException(this.id, "Try again!");
        }

        setAppointmentDate(date);
        return true;
    }

    @Override
    public Boolean cancelAppointment() throws DateLimitExceededException, PremiumAppointmentException {

        if (LocalDate.now().isAfter(this.appointmentDate)) {
            throw new DateLimitExceededException(this.id, this.appointmentDate, "The appointment date cannot be canceled after the expiration date.");
        }

        if (this.getId() < 0) {
            throw new PremiumAppointmentException(this.id, "Try again!");
        }

        this.appointmentDate = null;
        return true;
    }
}
