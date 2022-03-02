package ar.edu.itba.paw.webapp.helpers.validation.annotations;

import ar.edu.itba.paw.webapp.helpers.validation.implementations.UniqueClinicValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueClinicValidator.class)
public @interface ClinicExists {
    String message() default "clinic does not exist"; //si esta harcodeado lo toma, asi no

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
