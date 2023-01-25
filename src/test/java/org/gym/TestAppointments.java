package org.gym;


import org.gym.appointment.DefaultAppointment;
import org.gym.appointment.PremiumAppointment;
import org.gym.domain.Gym;
import org.gym.domain.Session;
import org.gym.domain.SessionType;
import org.gym.exceptions.GymException;
import org.gym.reports.GymReport;
import org.gym.users.Client;
import org.gym.users.Trainer;
import org.gym.utils.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TestAppointments {

    private Client client;
    private Trainer trainer;
    private Client otherClient;
    private LocalDate appointmentDay;

    @BeforeEach
    void setUp() {
        client = new Client("Shelia Burke", LocalDate.parse("2001-03-06"));
        trainer = new Trainer("Jamie Hopkins", LocalDate.parse("1997-01-18"));
        otherClient = new Client("Delores Phelps", LocalDate.parse("1982-02-05"));
        appointmentDay = LocalDate.now();
    }

    @Test
    public void testClient() {
        client.setName("Weird Name");
        client.setBirthday(LocalDate.of(2000,2, 20));
        assertEquals("Weird Name", client.getName());
        assertEquals(LocalDate.of(2000,2, 20), client.getBirthday());
    }

    @Test
    public void testTrainer() {
        trainer.setName("Weird Name");
        trainer.setBirthday(LocalDate.of(2000,2, 20));
        assertEquals("Weird Name", trainer.getName());
        assertEquals(LocalDate.of(2000,2, 20), trainer.getBirthday());
    }

    @Test
    public void testDefaultAppointment() {
        DefaultAppointment defaultAppointment = new DefaultAppointment(client, appointmentDay);
        assertEquals(client, defaultAppointment.getClient());
        assertEquals(50.0, defaultAppointment.getPrice());
        assertEquals(appointmentDay, defaultAppointment.getAppointmentDate());
        assertFalse(defaultAppointment.rescheduleAppointment(appointmentDay.plusDays(1)));
    }

    @Test
    public void testPremiumAppointment() {
        Client client = new Client("Shelia Burke", LocalDate.parse("2001-03-06"));
        LocalDate appointmentDay = LocalDate.now();
        PremiumAppointment premiumAppointment = new PremiumAppointment(client, appointmentDay);
        assertEquals(client, premiumAppointment.getClient());
        assertEquals(75.0, premiumAppointment.getPrice());
        assertEquals(appointmentDay, premiumAppointment.getAppointmentDate());
        assertTrue(premiumAppointment.rescheduleAppointment(appointmentDay.plusDays(1)));
    }

    @Test
    public void testClientAppointmentWithSession() throws GymException {
        Gym gym = new Gym();
        gym.addClient(client);

        GymReport gymReport = new GymReport();

        Session session = new Session(SessionType.YOGA);
        session.setType(SessionType.CYCLING);
        session.setTrainer(trainer);
        gym.addSession(session);

        DefaultAppointment defaultAppointment = new DefaultAppointment(client, appointmentDay);
        session.addAppointment(defaultAppointment);
        PremiumAppointment premiumAppointment = new PremiumAppointment(client, appointmentDay);
        session.addAppointment(premiumAppointment);
        DefaultAppointment defaultAppointmentLater = new DefaultAppointment(client, appointmentDay.plusDays(1));
        session.addAppointment(defaultAppointmentLater);

        assertEquals("Cycling", session.getType().getName());
        assertEquals(trainer, session.getTrainer());
        assertEquals(2, gymReport.getDefaultAppointmentsForClient(gym, client).size());
        assertEquals(1, gymReport.getPremiumAppointmentsForClient(gym, client).size());
    }

    @Test
    public void testClientAppointmentWithoutSession() throws GymException {
        Gym gym = new Gym();
        gym.addClient(client);

        GymReport gymReport = new GymReport();

        DefaultAppointment defaultAppointment = new DefaultAppointment(client, appointmentDay);
        PremiumAppointment premiumAppointment = new PremiumAppointment(client, appointmentDay);
        DefaultAppointment defaultAppointmentLater = new DefaultAppointment(client, appointmentDay.plusDays(1));

        assertEquals(0, gymReport.getDefaultAppointmentsForClient(gym, client).size());
        assertEquals(0, gymReport.getPremiumAppointmentsForClient(gym, client).size());
    }

    @Test
    public void testDuplicateSession() throws GymException {
        Gym gym = new Gym();
        gym.addClient(client);

        GymReport gymReport = new GymReport();

        Session session = new Session(SessionType.YOGA);

        GymException exception = assertThrows(GymException.class, () -> {
            gym.addSession(session);
            gym.addSession(session);
        });

        assertEquals(1, gym.getSessions().size());
        assertEquals(Status.EXISTING_SESSION, exception.getMessage());
    }

    @Test
    public void testDuplicateClient() throws GymException {
        Gym gym = new Gym();
        GymReport gymReport = new GymReport();

        GymException exception = assertThrows(GymException.class, () -> {
            gym.addClient(client);
            gym.addClient(client);
        });

        assertEquals(1, gym.getClients().size());
        assertEquals(Status.EXISTING_CLIENT, exception.getMessage());
    }
}
