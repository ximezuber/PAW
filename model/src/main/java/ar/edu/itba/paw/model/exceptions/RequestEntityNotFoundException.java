package ar.edu.itba.paw.model.exceptions;

public class RequestEntityNotFoundException extends NotFoundException {
    public RequestEntityNotFoundException(String entity) {
        super(entity + "-not-found");
    }
}
