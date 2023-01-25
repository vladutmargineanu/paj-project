package org.gym;


import org.gym.appointment.PremiumAppointment;
import org.gym.domain.Gym;
import org.gym.domain.Session;
import org.gym.domain.SessionType;
import org.gym.exceptions.AppointmentExistsException;
import org.gym.exceptions.GymException;
import org.gym.exceptions.NotAvailableDateException;
import org.gym.exceptions.PremiumAppointmentException;
import org.gym.reports.GymReport;
import org.gym.users.Client;
import org.gym.users.Trainer;
import org.gym.utils.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TestPremiumAppointments {

    private Client client;
    private Trainer trainer;
    private LocalDate appointmentDay;

    @BeforeEach
    void setUp() {
        client = new Client("Andrew Le", LocalDate.parse("2001-03-06"));
        trainer = new Trainer("Vlad Oli", LocalDate.parse("1997-01-18"));
        appointmentDay = LocalDate.now();
    }

    @Test
    public void testClient() {
        client.setName("Birth Name");
        client.setBirthday(LocalDate.of(2000, 2, 20));

        assertEquals("Birth Name", client.getName());
        assertEquals(LocalDate.of(2000, 2, 20), client.getBirthday());
    }

    @Test
    public void testTrainer() {
        trainer.setName("Birth Name");
        trainer.setBirthday(LocalDate.of(2000, 2, 20));

        assertEquals("Birth Name", trainer.getName());
        assertEquals(LocalDate.of(2000, 2, 20), trainer.getBirthday());
    }


    @Test
    public void testPremiumRescheduleAppointment() throws PremiumAppointmentException {
        Client client = new Client("Andrew Le", LocalDate.parse("2001-03-06"));
        LocalDate appointmentDay = LocalDate.now();
        PremiumAppointment premiumAppointment = new PremiumAppointment(1, client, appointmentDay);

        assertEquals(client, premiumAppointment.getClient());
        assertEquals(75.0, premiumAppointment.getPrice());
        assertEquals(appointmentDay, premiumAppointment.getAppointmentDate());
        assertTrue(premiumAppointment.rescheduleAppointment(appointmentDay.plusDays(1)));
    }

    @Test
    public void testPremiumRescheduleAppointmentPremiumAppointmentException() {
        Client client = new Client("Andrew Le", LocalDate.parse("2001-03-06"));
        LocalDate appointmentDay = LocalDate.now();
        PremiumAppointment premiumAppointment = new PremiumAppointment(-1, client, appointmentDay);

        PremiumAppointmentException thrown = Assertions.assertThrows(PremiumAppointmentException.class, () -> {
            premiumAppointment.rescheduleAppointment(appointmentDay);
        });

        assertEquals(client, premiumAppointment.getClient());
        assertEquals(75.0, premiumAppointment.getPrice());
        assertEquals(appointmentDay, premiumAppointment.getAppointmentDate());
        assertEquals("Try again!", thrown.getMessage());
        assertEquals(premiumAppointment.getId(), thrown.getId());    }

    @Test
    public void testPremiumCancelAppointment() throws NotAvailableDateException, PremiumAppointmentException {
        Client client = new Client("Andrew Le", LocalDate.parse("2001-03-06"));
        LocalDate appointmentDay = LocalDate.now();
        PremiumAppointment premiumAppointment = new PremiumAppointment(1, client, appointmentDay);

        assertEquals(client, premiumAppointment.getClient());
        assertEquals(75.0, premiumAppointment.getPrice());
        assertEquals(appointmentDay, premiumAppointment.getAppointmentDate());
        assertTrue(premiumAppointment.cancelAppointment());
    }

    @Test
    public void testPremiumCancelAppointmentNotAvailableDateException() {
        Client client = new Client("Andrew Le", LocalDate.parse("2001-03-06"));
        LocalDate appointmentDay = LocalDate.now();
        PremiumAppointment premiumAppointment = new PremiumAppointment(1, client, appointmentDay);

        NotAvailableDateException thrown = Assertions.assertThrows(NotAvailableDateException.class, () -> {
            premiumAppointment.rescheduleAppointment(appointmentDay.minusDays(2));
            premiumAppointment.cancelAppointment();
        });

        assertEquals(client, premiumAppointment.getClient());
        assertEquals(75.0, premiumAppointment.getPrice());
        assertNotEquals(appointmentDay, premiumAppointment.getAppointmentDate());
        assertEquals("The appointment date cannot be canceled after the expiration date.", thrown.getMessage());
        assertEquals(premiumAppointment.getId(), thrown.getId());
        assertEquals(premiumAppointment.getId(), thrown.getId());
        assertEquals(LocalDate.now(), thrown.getActualDate());
    }

    @Test
    public void testPremiumCancelAppointmentPremiumAppointmentException() {
        Client client = new Client("Andrew Le", LocalDate.parse("2001-03-06"));
        LocalDate appointmentDay = LocalDate.now();
        PremiumAppointment premiumAppointment = new PremiumAppointment(-1, client, appointmentDay);

        PremiumAppointmentException thrown = Assertions.assertThrows(PremiumAppointmentException.class, premiumAppointment::cancelAppointment);

        assertEquals(client, premiumAppointment.getClient());
        assertEquals(75.0, premiumAppointment.getPrice());
        assertEquals(appointmentDay, premiumAppointment.getAppointmentDate());
        assertEquals("Try again!", thrown.getMessage());
        assertEquals(premiumAppointment.getId(), thrown.getId());
    }

    @Test
    public void testPremiumDuplicateAppointmentException() throws AppointmentExistsException {

        PremiumAppointment premiumAppointment = new PremiumAppointment(1, client, appointmentDay);
        PremiumAppointment premiumAppointment2 = premiumAppointment;

        Session session = new Session(SessionType.YOGA);
        session.setType(SessionType.CYCLING);
        session.setTrainer(trainer);

        session.addAppointment(premiumAppointment);

        AppointmentExistsException thrown = assertThrows(AppointmentExistsException.class, () -> {
            session.addAppointment(premiumAppointment2);
        });

        assertEquals("Appointment already exists in the Session.", thrown.getMessage());
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

        PremiumAppointment premiumAppointment = new PremiumAppointment(1, client, appointmentDay);
        session.addAppointment(premiumAppointment);

        assertEquals("Cycling", session.getType().getName());
        assertEquals(trainer, session.getTrainer());
        assertEquals(1, gymReport.getPremiumAppointmentsForClient(gym, client).size());
    }

    @Test
    public void testClientAppointmentWithoutSession() throws GymException {
        Gym gym = new Gym();
        gym.addClient(client);

        GymReport gymReport = new GymReport();

        assertEquals(0, gymReport.getPremiumAppointmentsForClient(gym, client).size());
    }

    @Test
    public void testDuplicateSessionException() throws GymException {
        Gym gym = new Gym();
        gym.addClient(client);

        Session session = new Session(SessionType.YOGA);

        GymException exception = assertThrows(GymException.class, () -> {
            gym.addSession(session);
            gym.addSession(session);
        });

        assertEquals(1, gym.getSessions().size());
        assertEquals(Status.EXISTING_SESSION, exception.getMessage());
    }

    @Test
    public void testDuplicateClientException() {
        Gym gym = new Gym();

        GymException exception = assertThrows(GymException.class, () -> {
            gym.addClient(client);
            gym.addClient(client);
        });

        assertEquals(1, gym.getClients().size());
        assertEquals(Status.EXISTING_CLIENT, exception.getMessage());
    }
}
