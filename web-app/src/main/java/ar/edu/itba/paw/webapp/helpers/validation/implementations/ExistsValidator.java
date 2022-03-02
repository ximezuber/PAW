package ar.edu.itba.paw.webapp.helpers.validation.implementations;

import ar.edu.itba.paw.interfaces.service.ExistenceService;
import ar.edu.itba.paw.webapp.helpers.validation.annotations.Exists;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExistsValidator implements ConstraintValidator<Exists, String> {

    @Autowired
    private ExistenceService existenceService;

    private String field;

    @Override
    public void initialize(Exists constraintAnnotation) {
        this.field = constraintAnnotation.field();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !existenceService.exists(value, field);
    }
}
