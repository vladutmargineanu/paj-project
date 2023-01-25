package org.gym;

import org.gym.appointment.Appointment;
import org.gym.appointment.DefaultAppointment;
import org.gym.appointment.PremiumAppointment;
import org.gym.factory.AppointmentFactory;
import org.gym.factory.AppointmentType;
import org.gym.users.Client;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestFactory {
    @Test
    public void testInstanceDefaultAppointment() {
        Client client = new Client("Andrew Lee", LocalDate.parse("2001-03-06"));
        LocalDate birthday = LocalDate.now();

        Appointment defaultAppointment = AppointmentFactory.create(AppointmentType.DEFAULT, 1, client, birthday);
        assertThat(defaultAppointment, instanceOf(DefaultAppointment.class));
    }

    @Test
    public void testInstancePremiumAppointment() {
        Client client = new Client("Andrew Lee", LocalDate.parse("2001-03-06"));
        LocalDate birthday = LocalDate.now();

        Appointment premiumAppointment = AppointmentFactory.create(AppointmentType.PREMIUM, 1, client, birthday);
        assertThat(premiumAppointment, instanceOf(PremiumAppointment.class));
    }
}
