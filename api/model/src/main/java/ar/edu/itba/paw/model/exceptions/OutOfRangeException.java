package ar.edu.itba.paw.model.exceptions;

public class OutOfRangeException extends BadRequestException{
    public OutOfRangeException(String message) {
        super(message + "out-of-range");
    }
}
