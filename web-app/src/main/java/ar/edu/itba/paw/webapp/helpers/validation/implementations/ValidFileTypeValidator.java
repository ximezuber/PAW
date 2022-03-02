package ar.edu.itba.paw.webapp.helpers.validation.implementations;

import ar.edu.itba.paw.webapp.helpers.validation.annotations.ValidFileType;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidFileTypeValidator implements ConstraintValidator<ValidFileType, FormDataBodyPart> {


    private String[] types;

    @Override
    public void initialize(ValidFileType validFileType) {
        this.types = validFileType.types();
    }

    @Override
    public boolean isValid(FormDataBodyPart img, ConstraintValidatorContext constraintValidatorContext) {
        if(img == null) {
            return true;
        }

        String contentType = img.getMediaType().toString();
        for (String type : types) {
            if(type.equals(contentType)) {
                return true;
            }
        }

        return false;
    }
}
