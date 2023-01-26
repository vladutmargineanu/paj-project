package org.gym.service;

import org.gym.domain.Gym;
import org.gym.email.Email;
import org.gym.exceptions.EmailException;
import org.gym.users.Client;
import org.gym.utils.Status;

public class EmailNotifyService {

    private final EmailService emailService;

    public EmailNotifyService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void notifyMailer(Client client) {
        System.out.println(Status.SENDING_EMAIL_CLIENT + ": " + client.getName());

        if (emailService != null) {
            try {
                Email email = new Email(Gym.system, client, Status.REGISTRATION_COMPLETED, Status.WELCOME_MESSAGE + ", " + client.getName() + "!");

                emailService.sendNotificationEmail(email);

                Thread.sleep(100);
            } catch (EmailException | InterruptedException exception) {

                exception.printStackTrace();

                if (exception instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
