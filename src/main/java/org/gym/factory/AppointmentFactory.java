package org.gym.factory;

import org.gym.appointment.Appointment;
import org.gym.appointment.DefaultAppointment;
import org.gym.appointment.PremiumAppointment;
import org.gym.appointment.PremiumBenefit;
import org.gym.users.Client;

import java.time.LocalDate;

public class AppointmentFactory {
    private AppointmentFactory() {
        /* Instantiation not allowed! */
    }

    public static Appointment createAppointment(AppointmentType accountType, int id, Client client, LocalDate day, PremiumBenefit premiumBenefit) {
        if (accountType == null) {
            return null;
        }

        return switch (accountType) {
            case DEFAULT -> new DefaultAppointment(id, client, day);
            case PREMIUM -> new PremiumAppointment(id, client, day, premiumBenefit);
        };
    }
}

