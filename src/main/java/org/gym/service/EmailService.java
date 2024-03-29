package org.gym.service;

import org.gym.email.Email;
import org.gym.email.Queue;
import org.gym.exceptions.EmailException;
import org.gym.utils.Status;

public class EmailService implements Runnable {
    /*
     * Thread object - the worker - currentThread
     * The runnable (the job) is the EmailService instance (this)
     */
    private int sentEmails = 0;
    private boolean closedService;

    private final Thread currentThread;

    private final Queue<Email> queue = new Queue<>();

    public EmailService() {
        currentThread = new Thread(this);
        currentThread.start();
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
                    queue.wait();  // thread give up lock and wait
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
                queue.notify();  // notify waiting threads to resume
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
