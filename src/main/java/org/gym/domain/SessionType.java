package org.gym.domain;

public enum SessionType {
    YOGA("Yoga"),
    FARTLEK("Fartlek"),
    CYCLING("Cycling"),
    STRENGTH("Strength "),
    ENDURANCE("Endurance");

    private final String name;

    SessionType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
