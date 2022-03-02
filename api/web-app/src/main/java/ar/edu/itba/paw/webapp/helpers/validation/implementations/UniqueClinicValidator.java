package ar.edu.itba.paw.webapp.helpers.validation.implementations;

import ar.edu.itba.paw.interfaces.service.ClinicService;
import ar.edu.itba.paw.webapp.helpers.validation.annotations.ClinicExists;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueClinicValidator implements ConstraintValidator<ClinicExists, Integer> {

    @Autowired
    ClinicService clinicService;

    @Override
    public void initialize(ClinicExists constraintAnnotation) {
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return clinicService.getClinicById(value) != null;
    }
}
