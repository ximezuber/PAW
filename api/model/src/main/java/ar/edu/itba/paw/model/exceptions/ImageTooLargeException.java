package ar.edu.itba.paw.model.exceptions;

public class ImageTooLargeException extends UnprocessableEntityException {
    public ImageTooLargeException() {
        super("image-too-large");
    }
}
