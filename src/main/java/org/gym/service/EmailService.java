package org.gym.service;

import org.gym.email.Email;
import org.gym.email.Queue;
import org.gym.exceptions.EmailException;
import org.gym.utils.Status;

public class EmailService implements Runnable {

    private int sentEmails = 0;
    private boolean closedService;
    private final Queue queue = new Queue();

    public EmailService() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        Email email;
        while (true) {
            if (closedService) {
                return;
            }

            if ((email = queue.get()) != null) {
                sendEmail(email);
            }
            try {
                synchronized (queue) {
                    queue.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                return;
            }

        }
    }

    public int getSentEmails() {
        return sentEmails;
    }

    private void sendEmail(Email email) {
        System.out.println(email);
        sentEmails++;
    }

    public void sendNotificationEmail(Email email) throws EmailException {
        if (!closedService) {
            queue.add(email);
            synchronized (queue) {
                queue.notify();
            }
        } else
            throw new EmailException(Status.EMAIL_SERVICE_UNAVAILABLE);
    }

    public void close() {
        closedService = true;
        synchronized (queue) {
            queue.notify();
        }
    }

}
