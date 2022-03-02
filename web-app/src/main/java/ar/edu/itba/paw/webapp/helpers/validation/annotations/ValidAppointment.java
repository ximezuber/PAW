package ar.edu.itba.paw.webapp.helpers.validation.annotations;

import ar.edu.itba.paw.webapp.helpers.validation.implementations.AppointmentValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = AppointmentValidator.class)
public @interface ValidAppointment {
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String message() default "Appointment already exists";

    String license();

    String patient();

    String clinic();

    String day();

    String month();

    String year();

    String time();
}
