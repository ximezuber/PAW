package ar.edu.itba.paw.webapp.helpers.validation.implementations;

import ar.edu.itba.paw.webapp.helpers.validation.annotations.FieldMatch;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String field;
    private String fieldMatch;

    public void initialize(FieldMatch constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
    }

    public boolean isValid(Object value,ConstraintValidatorContext context) {

        Object fieldValue = new BeanWrapperImpl(value)
                .getPropertyValue(field);
        Object fieldMatchValue = new BeanWrapperImpl(value)
                .getPropertyValue(fieldMatch);



        if (fieldValue != null) {
            boolean isValid = fieldValue.equals(fieldMatchValue);

            if ( !isValid ) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate( "{user.password.not.matching}" )
                        .addNode( field ).addConstraintViolation();
            }

            return isValid;
        } else {
            return fieldMatchValue == null;
        }
    }
}
