package org.gym.factory;

import org.gym.appointment.Appointment;
import org.gym.appointment.DefaultAppointment;
import org.gym.appointment.PremiumAppointment;
import org.gym.users.Client;

import java.time.LocalDate;

public class AppointmentFactory {
    private AppointmentFactory() {
        /* Instantiation not allowed! */
    }

    public static Appointment create(AppointmentType accountType, int id, Client client, LocalDate day) {
        if (accountType.equals(AppointmentType.DEFAULT)) {
            return new DefaultAppointment(id, client, day);
        } else {
            return new PremiumAppointment(id, client, day);
        }
    }
}

