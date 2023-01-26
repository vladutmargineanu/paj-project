package org.gym.service;

import org.gym.domain.Gym;
import org.gym.domain.Session;
import org.gym.exceptions.GymException;
import org.gym.users.Client;

public class GymService {

    public static void addClient(Gym gym, Client client) throws GymException {
        gym.addClient(client);
    }

    public static void addSession(Gym gym, Session session) throws GymException {
        gym.addSession(session);
    }
}
