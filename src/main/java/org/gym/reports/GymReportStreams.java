package org.gym.reports;

import org.gym.appointment.Appointment;
import org.gym.appointment.DefaultAppointment;
import org.gym.appointment.PremiumAppointment;
import org.gym.domain.Gym;
import org.gym.domain.Session;
import org.gym.domain.SessionType;
import org.gym.users.Client;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GymReportStreams {

    public Map<SessionType, List<Session>> getSessionsByCategory(Gym gym) {
        return gym.getSessions().stream().collect(Collectors.groupingBy(Session::getType));
    }

    public Map<Session, Double> getSessionsByPayment(Gym gym) {
        return gym.getSessions().stream()
                .collect(Collectors.toMap(Function.identity(), session -> session.getAppointments().stream()
                        .mapToDouble(Appointment::getPrice).sum()));
    }

    public Map.Entry<Session, Double> getHighestPayingSession(Gym gym) {
        return getSessionsByPayment(gym).entrySet().stream().max(Map.Entry.comparingByValue()).get();
    }

    public Set<Client> getClientsForSession(Gym gym, Session session) {
        return getAppointmentsForSession(gym, session).stream().map(Appointment::getClient).collect(Collectors.toSet());
    }

    public List<Appointment> getAppointmentsForClient(Gym gym, Client client) {
        return getAppointments(gym).stream().filter(appointment -> appointment.getClient().equals(client)).toList();
    }

    public Map<Client, List<Appointment>> getAppointmentsByClient(Gym gym) {
        return gym.getClients().stream()
                .collect(Collectors.toMap(Function.identity(), client -> getAppointmentsForClient(gym, client)));
    }

    public List<Appointment> getAppointments(Gym gym) {
        return gym.getSessions().stream().map(Session::getAppointments).flatMap(Collection::stream).toList();
    }

    public List<Appointment> getAppointmentsForSession(Gym gym, Session session) {
        return gym.getSessions()
                .stream()
                .filter(s -> s.equals(session))
                .map(Session::getAppointments)
                .flatMap(Collection::stream)
                .toList();
    }

    public List<Appointment> getAppointmentsForADay(Gym gym, LocalDate day) {
        return getAppointments(gym).stream().filter(appointment -> appointment.getAppointmentDate().isEqual(day))
                .toList();
    }

    public List<Appointment> getDefaultAppointments(Gym gym) {
        return getAppointments(gym).stream().filter(DefaultAppointment.class::isInstance).toList();
    }

    public List<Appointment> getDefaultAppointmentsForClient(Gym gym, Client client) {
        return getAppointmentsForClient(gym, client).stream().filter(DefaultAppointment.class::isInstance).toList();
    }

    public List<Appointment> getDefaultAppointmentsForClientAtSession(Gym gym, Client client, Session session) {
        return getAppointmentsForSession(gym, session).stream()
                .filter(appointment -> appointment.getClient().equals(client))
                .filter(DefaultAppointment.class::isInstance).toList();
    }

    public Appointment getDefaultAppointmentForClientAtSessionInADay(Gym gym, Client client, Session session, LocalDate day) {
        List<Appointment> appointments = getAppointmentsForSession(gym, session).stream()
                .filter(appointment -> appointment.getClient().equals(client) && appointment.getAppointmentDate()
                        .isEqual(day))
                .filter(DefaultAppointment.class::isInstance).toList();

        if (appointments.isEmpty()) {
            return null;
        }
        return appointments.get(0);
    }

    public List<Appointment> getPremiumAppointments(Gym gym) {
        return getAppointments(gym).stream().filter(PremiumAppointment.class::isInstance).toList();
    }

    public List<Appointment> getPremiumAppointmentsForClient(Gym gym, Client client) {
        return getAppointmentsForClient(gym, client).stream().filter(PremiumAppointment.class::isInstance).toList();
    }

    public List<Appointment> getPremiumAppointmentsForClientAtSession(Gym gym, Client client, Session session) {
        return getAppointmentsForSession(gym, session).stream()
                .filter(appointment -> appointment.getClient().equals(client))
                .filter(PremiumAppointment.class::isInstance).toList();
    }

    public Appointment getPremiumAppointmentForClientAtSessionInADay(Gym gym, Client client, Session session, LocalDate day) {
        List<Appointment> appointments = getAppointmentsForSession(gym, session).stream()
                .filter(appointment -> appointment.getClient().equals(client) && appointment.getAppointmentDate()
                        .isEqual(day))
                .filter(PremiumAppointment.class::isInstance).toList();

        if (appointments.isEmpty()) {
            return null;
        }
        return appointments.get(0);
    }

}
