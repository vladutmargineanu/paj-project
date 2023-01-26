package org.gym;

import org.gym.appointment.Appointment;
import org.gym.appointment.DefaultAppointment;
import org.gym.appointment.PremiumAppointment;
import org.gym.domain.Gym;
import org.gym.domain.Session;
import org.gym.domain.SessionType;
import org.gym.exceptions.GymException;
import org.gym.reports.GymReportStreams;
import org.gym.service.GymService;
import org.gym.users.Client;
import org.gym.users.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.gym.appointment.PremiumBenefit.*;
import static org.junit.jupiter.api.Assertions.*;

class TestGymReportStreams {

    private Gym gym;
    private GymReportStreams gymReport;
    private Session yogaSession;
    private Session cyclingSession;
    private Session strengthSession;
    private Client client1;
    private Client client2;
    private Client client3;
    private Client client4;
    private Client client5;
    private LocalDate today;
    private LocalDate tomorrow;
    private LocalDate overmorrow;

    @BeforeEach
    void setUp() throws GymException {
        gym = new Gym();
        gymReport = new GymReportStreams();
        today = LocalDate.now();
        tomorrow = today.plusDays(1);
        overmorrow = today.plusDays(2);

        yogaSession = new Session(SessionType.YOGA);
        Trainer yogaTrainer = new Trainer("Andrew Lee", LocalDate.parse("1997-01-18"));
        yogaSession.setTrainer(yogaTrainer);
        GymService.addSession(gym, yogaSession);

        cyclingSession = new Session(SessionType.CYCLING);
        Trainer cyclingTrainer = new Trainer("Vlad Oli", LocalDate.parse("1993-09-16"));
        cyclingSession.setTrainer(cyclingTrainer);
        GymService.addSession(gym,cyclingSession);

        strengthSession = new Session(SessionType.STRENGTH);
        Trainer strengthTrainer = new Trainer("Bianca Tee", LocalDate.parse("1994-08-27"));
        strengthSession.setTrainer(strengthTrainer);
        GymService.addSession(gym,strengthSession);

        client1 = new Client("Cris Now", LocalDate.parse("2001-03-06"));
        GymService.addClient(gym, client1);
        client2 = new Client("Dani Vega", LocalDate.parse("1995-02-11"));
        GymService.addClient(gym, client2);
        client3 = new Client("Ali Lee", LocalDate.parse("1995-06-21"));
        GymService.addClient(gym, client3);
        client4 = new Client("Marie Curie", LocalDate.parse("1995-09-18"));
        GymService.addClient(gym, client4);
        client5 = new Client("Anna Aslan", LocalDate.parse("1998-03-27"));
        GymService.addClient(gym, client5);

        yogaSession.addAppointment(new DefaultAppointment(1, client1, today));
        yogaSession.addAppointment(new DefaultAppointment(3, client3, today));
        yogaSession.addAppointment(new PremiumAppointment(5, client5, today, SAUNA));
        strengthSession.addAppointment(new DefaultAppointment(1, client1, today));
        strengthSession.addAppointment(new DefaultAppointment(4, client4, today));
        cyclingSession.addAppointment(new DefaultAppointment(2, client2, today));
        cyclingSession.addAppointment(new PremiumAppointment(5, client5, today, SHOWER));

        yogaSession.addAppointment(new PremiumAppointment(2, client2, tomorrow, BODY_CHECK));
        yogaSession.addAppointment(new PremiumAppointment(3, client3, tomorrow, SAUNA));
        strengthSession.addAppointment(new DefaultAppointment(4, client4, tomorrow));
        strengthSession.addAppointment(new DefaultAppointment(5, client5, tomorrow));
        cyclingSession.addAppointment(new DefaultAppointment(1, client1, tomorrow));

        yogaSession.addAppointment(new PremiumAppointment(5, client5, overmorrow, SHOWER));
        strengthSession.addAppointment(new DefaultAppointment(5, client5, overmorrow));
        cyclingSession.addAppointment(new DefaultAppointment(1, client1, overmorrow));
        cyclingSession.addAppointment(new PremiumAppointment(2, client2, overmorrow, BODY_CHECK));
    }

    @Test
    void getSessionsByCategory() {
        Map<SessionType, List<Session>> sessionsByCategory = gymReport.getSessionsByCategory(gym);
        assertEquals(3, sessionsByCategory.keySet().size());
        assertEquals(1, sessionsByCategory.get(SessionType.YOGA).size());
        assertEquals(1, sessionsByCategory.get(SessionType.STRENGTH).size());
        assertEquals(1, sessionsByCategory.get(SessionType.CYCLING).size());
        assertNull(sessionsByCategory.get(SessionType.FARTLEK));
    }

    @Test
    void getSessionsByPayment() {
        Map<Session, Double> sessionsByPayment = gymReport.getSessionsByPayment(gym);
        assertEquals(3, sessionsByPayment.keySet().size());
        assertEquals(400.0, sessionsByPayment.get(yogaSession));
        assertEquals(250.0, sessionsByPayment.get(strengthSession));
        assertEquals(300.0, sessionsByPayment.get(cyclingSession));
    }

    @Test
    void getHighestPayingSession() {
        Map.Entry<Session, Double> highestPayingSession = gymReport.getHighestPayingSession(gym);
        assertEquals(yogaSession, highestPayingSession.getKey());
        assertEquals(400.0, highestPayingSession.getValue());
    }

    @Test
    void getClientsForSession() {
        assertEquals(4, gymReport.getClientsForSession(gym, yogaSession).size());
        assertEquals(3, gymReport.getClientsForSession(gym, strengthSession).size());
        assertEquals(3, gymReport.getClientsForSession(gym, cyclingSession).size());
    }

    @Test
    void getAppointmentsForClient() {
        assertEquals(4, gymReport.getAppointmentsForClient(gym, client1).size());
        assertEquals(3, gymReport.getAppointmentsForClient(gym, client2).size());
        assertEquals(2, gymReport.getAppointmentsForClient(gym, client3).size());
        assertEquals(2, gymReport.getAppointmentsForClient(gym, client4).size());
        assertEquals(5, gymReport.getAppointmentsForClient(gym, client5).size());
    }

    @Test
    void getAppointmentsByClient() {
        Map<Client, List<Appointment>> appointmentsByClient = gymReport.getAppointmentsByClient(gym);
        assertEquals(5, appointmentsByClient.keySet().size());
        assertEquals(4, appointmentsByClient.get(client1).size());
        assertEquals(3, appointmentsByClient.get(client2).size());
        assertEquals(2, appointmentsByClient.get(client3).size());
        assertEquals(2, appointmentsByClient.get(client4).size());
        assertEquals(5, appointmentsByClient.get(client5).size());
    }

    @Test
    void getAppointments() {
        List<Appointment> appointments = gymReport.getAppointments(gym);
        assertEquals(16, appointments.size());
    }

    @Test
    void getAppointmentsForSession() {
        assertEquals(6, gymReport.getAppointmentsForSession(gym, yogaSession).size());
        assertEquals(5, gymReport.getAppointmentsForSession(gym, strengthSession).size());
        assertEquals(5, gymReport.getAppointmentsForSession(gym, cyclingSession).size());
    }

    @Test
    void getAppointmentsForADay() {
        assertEquals(7 , gymReport.getAppointmentsForADay(gym, today).size());
        assertEquals(5 , gymReport.getAppointmentsForADay(gym, tomorrow).size());
        assertEquals(4 , gymReport.getAppointmentsForADay(gym, overmorrow).size());
    }

    @Test
    void getDefaultAppointments() {
        assertEquals(10, gymReport.getDefaultAppointments(gym).size());
    }

    @Test
    void getDefaultAppointmentsForClient() {
        assertEquals(4, gymReport.getDefaultAppointmentsForClient(gym, client1).size());
        assertEquals(1, gymReport.getDefaultAppointmentsForClient(gym, client2).size());
        assertEquals(1, gymReport.getDefaultAppointmentsForClient(gym, client3).size());
        assertEquals(2, gymReport.getDefaultAppointmentsForClient(gym, client4).size());
        assertEquals(2, gymReport.getDefaultAppointmentsForClient(gym, client5).size());
    }

    @Test
    void getDefaultAppointmentsForClientAtSession() {
        assertEquals(1 , gymReport.getDefaultAppointmentsForClientAtSession(gym, client1, yogaSession).size());
        assertEquals(2 , gymReport.getDefaultAppointmentsForClientAtSession(gym, client1, cyclingSession).size());
        assertEquals(0 , gymReport.getDefaultAppointmentsForClientAtSession(gym, client3, strengthSession).size());
        assertEquals(0 , gymReport.getDefaultAppointmentsForClientAtSession(gym, client5, cyclingSession).size());
    }

    @Test
    void getDefaultAppointmentForClientAtSessionInADay() {
        assertNull(gymReport.getDefaultAppointmentForClientAtSessionInADay(gym, client1, cyclingSession, today));
        assertNotNull( gymReport.getDefaultAppointmentForClientAtSessionInADay(gym, client1, cyclingSession, tomorrow));
        assertNotNull(gymReport.getDefaultAppointmentForClientAtSessionInADay(gym, client1, cyclingSession, overmorrow));
    }

    @Test
    void getPremiumAppointments() {
        assertEquals(6, gymReport.getPremiumAppointments(gym).size());
    }

    @Test
    void getPremiumAppointmentsForClient() {
        assertEquals(0, gymReport.getPremiumAppointmentsForClient(gym, client1).size());
        assertEquals(2, gymReport.getPremiumAppointmentsForClient(gym, client2).size());
        assertEquals(1, gymReport.getPremiumAppointmentsForClient(gym, client3).size());
        assertEquals(0, gymReport.getPremiumAppointmentsForClient(gym, client4).size());
        assertEquals(3, gymReport.getPremiumAppointmentsForClient(gym, client5).size());
    }

    @Test
    void getPremiumAppointmentsForClientAtSession() {
        assertEquals(1 , gymReport.getPremiumAppointmentsForClientAtSession(gym, client2, yogaSession).size());
        assertEquals(1 , gymReport.getPremiumAppointmentsForClientAtSession(gym, client2, cyclingSession).size());
        assertEquals(0 , gymReport.getPremiumAppointmentsForClientAtSession(gym, client5, strengthSession).size());
        assertEquals(1 , gymReport.getPremiumAppointmentsForClientAtSession(gym, client5, cyclingSession).size());
    }

    @Test
    void getPremiumAppointmentForClientAtSessionInADay() {
        assertNotNull( gymReport.getPremiumAppointmentForClientAtSessionInADay(gym, client5, cyclingSession, today));
        assertNull(gymReport.getPremiumAppointmentForClientAtSessionInADay(gym, client5, strengthSession, tomorrow));
        assertNotNull(gymReport.getPremiumAppointmentForClientAtSessionInADay(gym, client5, yogaSession, overmorrow));
    }
}