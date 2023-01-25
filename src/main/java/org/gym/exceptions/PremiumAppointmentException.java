package org.gym.exceptions;

public class PremiumAppointmentException extends GymException {
    private static final long serialVersionUID = -8368249553360028667L;

    int id;

    public PremiumAppointmentException(int id, String message) {
        super(message);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
