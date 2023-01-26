package org.gym;

import org.gym.appointment.DefaultAppointment;
import org.gym.domain.Gym;
import org.gym.domain.Session;
import org.gym.domain.SessionType;
import org.gym.exceptions.AppointmentExistsException;
import org.gym.exceptions.GymException;
import org.gym.exceptions.PremiumAppointmentException;
import org.gym.reports.GymReportStreams;
import org.gym.service.GymService;
import org.gym.users.Client;
import org.gym.users.Trainer;
import org.gym.utils.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestDefaultAppointments {

    private Client client;
    private Trainer trainer;
    private LocalDate appointmentDay;

    @BeforeEach
    void setUp() {
        client = new Client("Andrew Lee", LocalDate.parse("2001-03-06"));
        trainer = new Trainer("Vlad Oli", LocalDate.parse("1997-01-18"));
        appointmentDay = LocalDate.now();
    }

    @Test
    public void testClient() {
        client.setName("Birth Name");
        client.setBirthday(LocalDate.of(2000,2, 20));

        assertEquals("Birth Name", client.getName());
        assertEquals(LocalDate.of(2000,2, 20), client.getBirthday());
    }

    @Test
    public void testTrainer() {
        trainer.setName("Birth Name");
        trainer.setBirthday(LocalDate.of(2000,2, 20));

        assertEquals("Birth Name", trainer.getName());
        assertEquals(LocalDate.of(2000,2, 20), trainer.getBirthday());
    }

    @Test
    public void testDefaultRescheduleAppointmentException() {

        DefaultAppointment defaultAppointment = new DefaultAppointment(1, client, appointmentDay);
        PremiumAppointmentException thrown = Assertions.assertThrows(PremiumAppointmentException.class, () -> {
            defaultAppointment.rescheduleAppointment(appointmentDay.plusDays(1));
        });

        assertEquals(client, defaultAppointment.getClient());
        assertEquals(50.0, defaultAppointment.getPrice());
        assertEquals(appointmentDay, defaultAppointment.getAppointmentDate());
        assertEquals("Only PREMIUM appointments can be rescheduled.", thrown.getMessage());
        assertEquals(defaultAppointment.getId(), thrown.getId());

    }

    @Test
    public void testDefaultCancelAppointmentException() {

        DefaultAppointment defaultAppointment = new DefaultAppointment(1, client, appointmentDay);
        PremiumAppointmentException thrown = assertThrows(PremiumAppointmentException.class, defaultAppointment::cancelAppointment);

        assertEquals(client, defaultAppointment.getClient());
        assertEquals(50.0, defaultAppointment.getPrice());
        assertEquals(appointmentDay, defaultAppointment.getAppointmentDate());
        assertEquals("Only PREMIUM appointments can be canceled.", thrown.getMessage());
        assertEquals(defaultAppointment.getId(), thrown.getId());
    }

    @Test
    public void testDefaultDuplicateAppointmentException() throws GymException {

        DefaultAppointment defaultAppointment = new DefaultAppointment(1, client, appointmentDay);

        Session session = new Session(SessionType.YOGA);
        session.setType(SessionType.CYCLING);
        session.setTrainer(trainer);

        session.addAppointment(defaultAppointment);

        AppointmentExistsException thrown = assertThrows(AppointmentExistsException.class, () -> {
            session.addAppointment(defaultAppointment);
        });

        assertEquals("Appointment already exists in the Session.", thrown.getMessage());
    }


    @Test
    public void testClientAppointmentWithSession() throws GymException {
        Gym gym = new Gym();
        GymService.addClient(gym, client);

        GymReportStreams gymReport = new GymReportStreams();

        Session session = new Session(SessionType.YOGA);
        session.setType(SessionType.CYCLING);
        session.setTrainer(trainer);
        GymService.addSession(gym, session);

        DefaultAppointment defaultAppointment = new DefaultAppointment(1, client, appointmentDay);
        session.addAppointment(defaultAppointment);

        DefaultAppointment defaultAppointmentLater = new DefaultAppointment(2, client, appointmentDay.plusDays(1));
        session.addAppointment(defaultAppointmentLater);

        assertEquals("Cycling", session.getType().getName());
        assertEquals(trainer, session.getTrainer());
        assertEquals(2, gymReport.getDefaultAppointmentsForClient(gym, client).size());
    }

    @Test
    public void testClientAppointmentWithoutSession() throws GymException {
        Gym gym = new Gym();
        GymService.addClient(gym, client);

        GymReportStreams gymReport = new GymReportStreams();

        assertEquals(0, gymReport.getDefaultAppointmentsForClient(gym, client).size());
    }

    @Test
    public void testDuplicateSessionException() throws GymException {
        Gym gym = new Gym();
        GymService.addClient(gym, client);

        Session session = new Session(SessionType.YOGA);

        GymException exception = assertThrows(GymException.class, () -> {
            GymService.addSession(gym, session);
            GymService.addSession(gym, session);
        });

        assertEquals(1, gym.getSessions().size());
        assertEquals(Status.EXISTING_SESSION, exception.getMessage());
    }

    @Test
    public void testDuplicateClientException()  {
        Gym gym = new Gym();

        GymException exception = assertThrows(GymException.class, () -> {
            GymService.addClient(gym, client);
            GymService.addClient(gym, client);
        });

        assertEquals(1, gym.getClients().size());
        assertEquals(Status.EXISTING_CLIENT, exception.getMessage());
    }
}
