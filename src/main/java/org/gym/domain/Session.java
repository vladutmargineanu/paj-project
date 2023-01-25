package org.gym.domain;

import lombok.Getter;
import lombok.Setter;
import org.gym.appointment.Appointment;
import org.gym.exceptions.AppointmentExistsException;
import org.gym.users.Trainer;

import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
public class Session {

    private Trainer trainer;
    private SessionType type;

    private final Set<Appointment> appointments;

    public Session(SessionType type) {
        this.type = type;
        this.appointments = new LinkedHashSet<>();
    }

    public void addAppointment(final Appointment appointment) throws AppointmentExistsException {
        if (appointments.contains(appointment)) {
            throw new AppointmentExistsException("Appointment already exists in the Session.");
        }
        this.appointments.add(appointment);
    }

}
