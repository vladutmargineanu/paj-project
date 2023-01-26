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

    private final Client client;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractAppointment that)) return false;

        if (getId() != that.getId()) return false;
        if (getPrice() != null ? !getPrice().equals(that.getPrice()) : that.getPrice() != null) return false;
        if (getClient() != null ? !getClient().equals(that.getClient()) : that.getClient() != null) return false;
        return getAppointmentDate() != null ? getAppointmentDate().equals(that.getAppointmentDate()) : that.getAppointmentDate() == null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + getId();
        return result;
    }
}
