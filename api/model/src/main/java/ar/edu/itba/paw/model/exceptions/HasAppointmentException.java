package ar.edu.itba.paw.model.exceptions;

public class HasAppointmentException extends ConflictException{
    public HasAppointmentException(String message) {
        super(message + "-already-has-appointment");
    }
}
