package ar.edu.itba.paw.model.exceptions;

public class DateInPastException extends BadRequestException {

    public DateInPastException(){
        super("past-date");
    }
}
