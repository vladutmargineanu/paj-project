package org.gym;

import org.gym.domain.Gym;
import org.gym.email.Email;
import org.gym.exceptions.EmailException;
import org.gym.service.EmailService;
import org.gym.users.Client;
import org.gym.utils.Status;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestEmail {

    @Test
    public void testSendMail() throws InterruptedException {
        Client client = new Client("Andrew Lee", LocalDate.parse("2001-03-06"));

        EmailService emailService = new EmailService();
        for (int i = 0; i < 10; i++) {
            try {
                Email email = new Email(Gym.system, client, Status.REGISTRATION_COMPLETED, Status.WELCOME_MESSAGE + ", " + client.getName() + "!");

                emailService.sendNotificationEmail(email);

                Thread.sleep(100);
            } catch (EmailException e) {
                e.printStackTrace();
            }
        }

        assertEquals(10, emailService.getSentEmails());
    }

    @Test
    public void testSendMailClosedService() throws EmailException {
        Client client = new Client("Andrew Lee", LocalDate.parse("2001-03-06"));

        EmailService emailService = new EmailService();
        emailService.close();

        EmailException exception = assertThrows(EmailException.class, () -> {
            Email email = new Email(Gym.system, client, Status.REGISTRATION_COMPLETED, Status.WELCOME_MESSAGE + ", " + client.getName() + "!");

            emailService.sendNotificationEmail(email);

            Thread.sleep(100);
        });

        assertEquals(Status.EMAIL_SERVICE_UNAVAILABLE, exception.getMessage());
        assertEquals(0, emailService.getSentEmails());
    }

    @Test
    public void testSendMailInterruptedException() throws InterruptedException {
        Client client = new Client("Andrew Lee", LocalDate.parse("2001-03-06"));

        EmailService emailService = new EmailService();

        InterruptedException exception = assertThrows(InterruptedException.class, () -> {
            Email email = new Email(Gym.system, client, Status.REGISTRATION_COMPLETED, Status.WELCOME_MESSAGE + ", " + client.getName() + "!");

            Thread.currentThread().interrupt();
            emailService.sendNotificationEmail(email);

            Thread.sleep(100);
        });

        assertEquals("sleep interrupted", exception.getMessage());
        assertEquals(0, emailService.getSentEmails());
    }
}
