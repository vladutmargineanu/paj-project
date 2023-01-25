package org.gym.email;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Queue {

    private List<Email> emails = Collections.synchronizedList(new LinkedList<Email>());

    public void add(Email email) {
        emails.add(email);
    }

    public Email get() {
        if (!emails.isEmpty()) {
            return emails.remove(emails.size() - 1);
        }

        return null;
    }

}
