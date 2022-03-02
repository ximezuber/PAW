package ar.edu.itba.paw.model.exceptions;

public class EntityNotFoundException extends NotFoundException {
    public EntityNotFoundException(String entity) {
        super(entity + "-not-found");
    }
}
