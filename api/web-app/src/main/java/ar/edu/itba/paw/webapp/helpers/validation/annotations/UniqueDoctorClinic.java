package ar.edu.itba.paw.webapp.helpers.validation.annotations;

import ar.edu.itba.paw.webapp.helpers.validation.implementations.DoctorClinicValidator;

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
@Constraint(validatedBy = DoctorClinicValidator.class)
@Documented
public @interface UniqueDoctorClinic {

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String message() default "passwords dont match";

    String clinic();
}
