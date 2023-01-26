package org.gym.domain;

import lombok.Getter;
import lombok.Setter;
import org.gym.appointment.Appointment;
import org.gym.exceptions.AppointmentExistsException;
import org.gym.exceptions.GymException;
import org.gym.users.Trainer;

import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
public class Session {

    private static final int MAX_APPOINTMENTS = 25;

    private Trainer trainer;
    private SessionType type;

    private final Set<Appointment> appointments;

    public Session(SessionType type) {
        this.type = type;
        this.appointments = new LinkedHashSet<>();
    }

    public void addAppointment(final Appointment appointment) throws GymException {
        if (appointments.size() == MAX_APPOINTMENTS) {
            throw new GymException("There are no more free seats for the session.");
        }

        if (appointments.contains(appointment)) {
            throw new AppointmentExistsException("Appointment already exists in the Session.");
        }

        this.appointments.add(appointment);
    }

    @Override
    public String toString() {
        return "Session{" +
                "trainer=" + trainer +
                ", type=" + type +
                ", appointments=" + appointments +
                '}';
    }

}
