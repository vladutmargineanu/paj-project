package org.gym.utils;

public final class Status {

    /* Generic */
    public static final String WELCOME_MESSAGE = "WELCOME";
    public static final String INVALID_OPERATION = "INVALID OPERATION";
    public static final String REGISTRATION_COMPLETED = "REGISTRATION COMPLETED";

    /* Client */
    public static final String EXISTING_CLIENT = "EXISTING CLIENT";

    /* Session */
    public static final String EXISTING_SESSION = "EXISTING SESSION";

    /* Email */
    public static final String SENDING_EMAIL_CLIENT = "SENDING EMAIL FOR CLIENT";
    public static final String EMAIL_SERVICE_UNAVAILABLE = "EMAIL SERVICE UNAVAILABLE";

    private Status() {
        /* Instantiation not allowed! */
    }
}
