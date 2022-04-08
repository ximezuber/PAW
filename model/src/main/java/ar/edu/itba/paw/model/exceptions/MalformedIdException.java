package ar.edu.itba.paw.model.exceptions;

public class MalformedIdException extends BadRequestException {

    public MalformedIdException(String entity) {
        super(entity + "-id-malformed");
    }
}
