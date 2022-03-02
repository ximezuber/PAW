package ar.edu.itba.paw.webapp.helpers.validation.implementations;

import ar.edu.itba.paw.interfaces.service.ExistenceService;
import ar.edu.itba.paw.webapp.helpers.validation.annotations.Unique;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueValidator implements ConstraintValidator<Unique,String> {

    @Autowired
    private ExistenceService existenceService;

    private String field;

    public void initialize(Unique constraintAnnotation) {
        this.field = constraintAnnotation.field();
    }

    public boolean isValid(String value, ConstraintValidatorContext context){
        return existenceService.exists(value,field);
    }
}
