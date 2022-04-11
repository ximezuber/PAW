package ar.edu.itba.paw.webapp.helpers.validation.implementations;

import ar.edu.itba.paw.interfaces.service.PrepaidToClinicService;
import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.Prepaid;
import ar.edu.itba.paw.webapp.helpers.validation.annotations.UniquePrepaidToClinic;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PrepaidToClinicValidator implements ConstraintValidator<UniquePrepaidToClinic,Object> {

    private String prepaid;

    private String clinic;

    @Autowired
    PrepaidToClinicService prepaidToClinicService;

    public void initialize(UniquePrepaidToClinic constraintAnnotation) {
        this.prepaid = constraintAnnotation.prepaid();
        this.clinic = constraintAnnotation.clinic();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object clinicValue = new BeanWrapperImpl(value)
                .getPropertyValue(clinic);
        Object prepaidValue = new BeanWrapperImpl(value)
                .getPropertyValue(prepaid);

        boolean valid = !prepaidToClinicService.clinicHasPrepaid((String)prepaidValue,(int)clinicValue);
        if ( !valid ) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate( "{value.registered}")
                    .addNode(clinic).addConstraintViolation();
        }
        return valid;
    }
}
