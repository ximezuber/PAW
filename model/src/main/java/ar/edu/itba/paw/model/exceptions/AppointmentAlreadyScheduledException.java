package ar.edu.itba.paw.model.exceptions;

public class AppointmentAlreadyScheduledException extends ConflictException {
    public AppointmentAlreadyScheduledException(){
        super("appointment-exists");
    }
}
