package org.gym.domain;

import lombok.Getter;
import lombok.Setter;
import org.gym.exceptions.GymException;
import org.gym.service.EmailNotifyService;
import org.gym.service.EmailService;
import org.gym.users.Client;
import org.gym.utils.Status;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class Gym {
    public static final Client system = new Client("System", null);

    private Set<Client> clients = new HashSet<>();
    private Set<Session> sessions = new HashSet<>();
    private EmailService emailService = new EmailService();
    private EmailNotifyService emailNotifyService = new EmailNotifyService(emailService);

    public void addClient(final Client client) throws GymException {
        if (clients.contains(client)) {
            throw new GymException(Status.EXISTING_CLIENT);
        } else {
            clients.add(client);
            emailNotifyService.notify(client);
        }
    }

    public void addSession(Session session) throws GymException {
        if (sessions.contains(session)) {
            throw new GymException(Status.EXISTING_SESSION);
        } else {
            sessions.add(session);
        }
    }

}
