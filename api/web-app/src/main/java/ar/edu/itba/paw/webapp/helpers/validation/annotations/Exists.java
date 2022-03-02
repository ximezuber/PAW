package ar.edu.itba.paw.webapp.helpers.validation.annotations;

import ar.edu.itba.paw.webapp.helpers.validation.implementations.ExistsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = ExistsValidator.class)
public @interface Exists {
    String message() default "entity-not-exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return The first field
     */

    String field();
}
