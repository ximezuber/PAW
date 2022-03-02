package ar.edu.itba.paw.model.exceptions;

public class FavouriteExistsException extends ConflictException {
    public FavouriteExistsException() {
        super("favorite-exists");
    }
}
