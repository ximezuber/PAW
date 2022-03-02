package ar.edu.itba.paw.model.exceptions;

public class EntityDependencyException extends ConflictException{
    public EntityDependencyException(String message) {
        super(message + "-dependency");
    }
}
