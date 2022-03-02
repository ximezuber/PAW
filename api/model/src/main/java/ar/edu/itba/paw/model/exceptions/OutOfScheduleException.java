package ar.edu.itba.paw.model.exceptions;

public class OutOfScheduleException extends ConflictException {

    public OutOfScheduleException() {
        super("out-of-schedule");
    }
}
