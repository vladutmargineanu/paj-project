package org.gym.domain;

import lombok.Getter;
import lombok.Setter;
import org.gym.exceptions.GymException;
import org.gym.service.EmailNotifyService;
import org.gym.service.EmailService;
import org.gym.users.Client;
import org.gym.utils.ClientRegistrationListener;
import org.gym.utils.Status;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
public class Gym {
    public static final Client system = new Client("System", null);

    private Set<Client> clients = new HashSet<>();
    private Set<Session> sessions = new HashSet<>();
    private EmailService emailService = new EmailService();
    private final List<ClientRegistrationListener> listeners = new ArrayList<>();
    private EmailNotifyService emailNotifyService = new EmailNotifyService(emailService);
    private int printedClients = 0;

    public Gym() {
        listeners.add(client -> {
            System.out.println("Client added: " + client.getName());
            printedClients++;
        });
    }

    public void addClient(final Client client) throws GymException {
        if (clients.contains(client)) {
            throw new GymException(Status.EXISTING_CLIENT);
        } else {
            clients.add(client);
            emailNotifyService.notifyMailer(client);
            notifySystem(client);
        }
    }

    public void addSession(Session session) throws GymException {
        if (sessions.contains(session)) {
            throw new GymException(Status.EXISTING_SESSION);
        } else {
            sessions.add(session);
        }
    }

    private void notifySystem(Client client) {
        for (ClientRegistrationListener listener : listeners) {
            listener.onClientAdded(client);
        }
    }

}
