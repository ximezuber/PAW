package ar.edu.itba.paw.webapp.helpers.validation.annotations;

import ar.edu.itba.paw.webapp.helpers.validation.implementations.ValidFileTypeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidFileTypeValidator.class)
public @interface ValidFileType {
    String message() default "Invalid format, only png or jpg accepted";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String[] types();
}
