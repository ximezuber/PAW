package ar.edu.itba.paw.model.exceptions;

public class NoPrepaidNumberException extends BadRequestException{
    public NoPrepaidNumberException() {
        super("prepaid-number-required");
    }
}
