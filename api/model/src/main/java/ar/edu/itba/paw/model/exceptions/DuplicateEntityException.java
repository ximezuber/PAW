package ar.edu.itba.paw.model.exceptions;

public class DuplicateEntityException extends ConflictException {
    public DuplicateEntityException(String message) {
        super(message);
    }
}
