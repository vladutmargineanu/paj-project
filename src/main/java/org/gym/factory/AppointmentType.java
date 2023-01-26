package org.gym.factory;

public enum AppointmentType {
    DEFAULT("default"),
    PREMIUM("premium");

    private final String appointmentType;

    AppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    @Override
    public String toString() {
        return "AppointmentType{" +
                "appointmentType='" + appointmentType + '\'' +
                '}';
    }

    public String getAppointmentType() {
        return appointmentType;
    }
}
