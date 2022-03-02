package ar.edu.itba.paw.model.exceptions;

public class ImageInfoMissingException extends BadRequestException {
    public ImageInfoMissingException() {
        super("image-info-missing");
    }
}
