package org.gym.reports;

import org.gym.appointment.Appointment;
import org.gym.appointment.DefaultAppointment;
import org.gym.appointment.PremiumAppointment;
import org.gym.domain.Gym;
import org.gym.domain.Session;
import org.gym.domain.SessionType;
import org.gym.users.Client;

import java.time.LocalDate;
import java.util.*;

public class GymReportCollections {

    public Map<SessionType, List<Session>> getSessionsByCategory(Gym gym) {
        SortedSet<SessionType> sessionTypes = new TreeSet<>(Comparator.comparing(SessionType::getName));
        Map<SessionType, List<Session>> sessionsByCategory = new TreeMap<>();
        gym.getSessions().forEach(session -> sessionTypes.add(session.getType()));

        for (SessionType sessionType : sessionTypes) {
            List<Session> sessions = new ArrayList<>();
            for (Session session : gym.getSessions()) {
                if (session.getType().equals(sessionType)) {
                    sessions.add(session);
                }
            }
            sessionsByCategory.put(sessionType, sessions);
        }
        return sessionsByCategory;
    }


    public Map<Session, Double> getSessionsByPayment(Gym gym) {
        Map<Session, Double> sessionsByPayment = new TreeMap<>(Comparator.comparing(Session::getType));

        for (Session session : gym.getSessions()) {
            Double sum = 0.0;
            for (Appointment appointment : session.getAppointments()) {
                sum += appointment.getPrice();
            }
            sessionsByPayment.put(session, sum);
        }
        return sessionsByPayment;
    }

    public Map.Entry<Session, Double> getHighestPayingSession(Gym gym) {
        Map<Session, Double> sessionsByPayment = getSessionsByPayment(gym);

        Session session = Collections.max(sessionsByPayment.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getKey();
        Double price = sessionsByPayment.get(session);

        return new AbstractMap.SimpleEntry<>(session, price);
    }

    public Set<Appointment> getAppointmentsForSession(Gym gym, Session session) {

        for (Session session1 : gym.getSessions()) {
            if (session1.equals(session)) {
                return session.getAppointments();
            }
        }
        return null;
    }

    public Set<Client> getClientsForSession(Gym gym, Session session) {
        Set<Client> clients = new HashSet<>();
        Set<Appointment> appointments = getAppointmentsForSession(gym, session);

        for (Appointment appointment : appointments) {
            clients.add(appointment.getClient());
        }
        return clients;
    }

    public List<Appointment> getAppointmentsForClient(Gym gym, Client client) {
        List<Appointment> clientAppointments = new ArrayList<>();
        List<Appointment> appointments = getAppointments(gym);

        for (Appointment appointment : appointments) {
            if (appointment.getClient().equals(client)) {
                clientAppointments.add(appointment);
            }
        }
        return clientAppointments;
    }

    public Map<Client, List<Appointment>> getAppointmentsByClient(Gym gym) {
        SortedSet<Client> clients = new TreeSet<>(Comparator.comparing(Client::getName));
        Map<Client, List<Appointment>> appointmentsByClient = new TreeMap<>(Comparator.comparing(Client::getName));
        clients.addAll(gym.getClients());

        for (Client client : clients) {
            List<Appointment> appointments = new ArrayList<>();
            for (Appointment appointment : getAppointments(gym)) {
                if (appointment.getClient().equals(client)) {
                    appointments.add(appointment);
                }
            }
            appointmentsByClient.put(client, appointments);
        }
        return appointmentsByClient;
    }

    public List<Appointment> getAppointments(Gym gym) {
        List<Appointment> appointments = new ArrayList<>();

        for (Session session : gym.getSessions()) {
            appointments.addAll(session.getAppointments());
        }
        return appointments;
    }


    public List<Appointment> getAppointmentsForADay(Gym gym, LocalDate day) {
        List<Appointment> appointmentsForADay = new ArrayList<>();
        List<Appointment> appointments = getAppointments(gym);

        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentDate().equals(day)) {
                appointmentsForADay.add(appointment);
            }
        }
        return appointmentsForADay;
    }

    public List<Appointment> getDefaultAppointments(Gym gym) {
        List<Appointment> defaultAppointments = new ArrayList<>();
        List<Appointment> appointments = getAppointments(gym);

        for (Appointment appointment : appointments) {
            if (appointment instanceof DefaultAppointment) {
                defaultAppointments.add(appointment);
            }
        }
        return defaultAppointments;
    }

    public List<Appointment> getDefaultAppointmentsForClient(Gym gym, Client client) {
        List<Appointment> defaultAppointmentsForClient = new ArrayList<>();
        List<Appointment> appointments = getAppointmentsForClient(gym, client);

        for (Appointment appointment : appointments) {
            if (appointment instanceof DefaultAppointment) {
                defaultAppointmentsForClient.add(appointment);
            }
        }
        return defaultAppointmentsForClient;
    }

    public List<Appointment> getDefaultAppointmentsForClientAtSession(Gym gym, Client client, Session session) {
        List<Appointment> defaultAppointmentsForClientAtSession = new ArrayList<>();
        Set<Appointment> appointmentsForSession = getAppointmentsForSession(gym, session);

        for (Appointment appointment : appointmentsForSession) {
            if (appointment.getClient().equals(client)) {
                if (appointment instanceof DefaultAppointment) {
                    defaultAppointmentsForClientAtSession.add(appointment);
                }
            }
        }
        return defaultAppointmentsForClientAtSession;
    }

    public Appointment getDefaultAppointmentForClientAtSessionInADay(Gym gym, Client client, Session session, LocalDate day) {
        List<Appointment> defaultAppointmentForClientAtSessionInADay = new ArrayList<>();
        List<Appointment> defaultAppointmentsForClientAtSession = getDefaultAppointmentsForClientAtSession(gym, client, session);

        for (Appointment appointment : defaultAppointmentsForClientAtSession) {
            if (appointment.getAppointmentDate().equals(day)) {
                defaultAppointmentForClientAtSessionInADay.add(appointment);
            }
        }

        if (defaultAppointmentForClientAtSessionInADay.isEmpty()) {
            return null;
        }
        return defaultAppointmentForClientAtSessionInADay.get(0);
    }

    public List<Appointment> getPremiumAppointments(Gym gym) {
        List<Appointment> premiumAppointments = new ArrayList<>();
        List<Appointment> appointments = getAppointments(gym);

        for (Appointment appointment : appointments) {
            if (appointment instanceof PremiumAppointment) {
                premiumAppointments.add(appointment);
            }
        }
        return premiumAppointments;
    }

    public List<Appointment> getPremiumAppointmentsForClient(Gym gym, Client client) {
        List<Appointment> premiumAppointmentsForClient = new ArrayList<>();
        List<Appointment> appointments = getAppointmentsForClient(gym, client);

        for (Appointment appointment : appointments) {
            if (appointment instanceof PremiumAppointment) {
                premiumAppointmentsForClient.add(appointment);
            }
        }
        return premiumAppointmentsForClient;
    }

    public List<Appointment> getPremiumAppointmentsForClientAtSession(Gym gym, Client client, Session session) {
        List<Appointment> premiumAppointmentsForClientAtSession = new ArrayList<>();
        Set<Appointment> appointmentsForSession = getAppointmentsForSession(gym, session);

        for (Appointment appointment : appointmentsForSession) {
            if (appointment.getClient().equals(client)) {
                if (appointment instanceof PremiumAppointment) {
                    premiumAppointmentsForClientAtSession.add(appointment);
                }
            }
        }
        return premiumAppointmentsForClientAtSession;
    }

    public Appointment getPremiumAppointmentForClientAtSessionInADay(Gym gym, Client client, Session session, LocalDate day) {
        List<Appointment> premiumAppointmentForClientAtSessionInADay = new ArrayList<>();
        List<Appointment> premiumAppointmentsForClientAtSession = getPremiumAppointmentsForClientAtSession(gym, client, session);

        for (Appointment appointment : premiumAppointmentsForClientAtSession) {
            if (appointment.getAppointmentDate().equals(day)) {
                premiumAppointmentForClientAtSessionInADay.add(appointment);
            }
        }

        if (premiumAppointmentForClientAtSessionInADay.isEmpty()) {
            return null;
        }
        return premiumAppointmentForClientAtSessionInADay.get(0);
    }
}
