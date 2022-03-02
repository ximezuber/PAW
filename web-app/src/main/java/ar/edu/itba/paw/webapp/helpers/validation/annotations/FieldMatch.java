package ar.edu.itba.paw.webapp.helpers.validation.annotations;


import ar.edu.itba.paw.webapp.helpers.validation.implementations.FieldMatchValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = FieldMatchValidator.class)
@Documented
public @interface FieldMatch {
    String message() default "passwords dont match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return The first field
     */

    String field();

    /**
     * @return The second field
     */

    String fieldMatch();

    /**
     * Defines several <code>@FieldMatch</code> annotations on the same element
     *
     * @FieldsValueMatch.List({
     *     @FieldsValueMatch(field ,fieldMatch),
     *     @FieldsValueMatch(field ,fieldMatch)
     * })
     *
     * @see FieldMatch
     */
    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List
    {
        FieldMatch[] value();
    }
}
