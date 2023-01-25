package org.gym.exceptions;

public class AppointmentExistsException extends GymException {
	private static final long serialVersionUID = -8368249553360028667L;

	public AppointmentExistsException(String message) {
		super(message);
	}

}
