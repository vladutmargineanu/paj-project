package org.gym.email;

import lombok.Getter;
import lombok.Setter;
import org.gym.users.Client;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Setter
@Getter
public class Email {

    private Client from;
    private String body;
    private String title;
    private List<Client> to;

    public Email(Client from, Client to, String title, String body) {
        this.from = from;
        this.body = body;
        this.title = title;
        addToClient(to);
    }

    public void addToClient(Client client) {
        setTo(Collections.singletonList(client));
    }

    @Override
    public String toString() {
        List<Client> clients = getTo();

        StringBuilder clientsTo = new StringBuilder();

        for (Client c : clients) {
            clientsTo.append(c.getName());
        }

        return "SENDING EMAIL..." + "\n" +
                "From: " + getFrom().getName() + "\n" +
                "To: " + clientsTo + "\n" +
                "Title: " + getTitle() + "\n" +
                "Body: " + getBody() + "\n";
    }
}