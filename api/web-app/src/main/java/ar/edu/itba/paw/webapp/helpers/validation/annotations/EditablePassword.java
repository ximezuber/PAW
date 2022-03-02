package ar.edu.itba.paw.webapp.helpers.validation.annotations;


import ar.edu.itba.paw.webapp.helpers.validation.implementations.EditablePasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EditablePasswordValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EditablePassword {
    String message() default "password too short";//"{user.password.too.short}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};


}
