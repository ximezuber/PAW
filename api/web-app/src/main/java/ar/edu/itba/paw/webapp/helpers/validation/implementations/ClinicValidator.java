package ar.edu.itba.paw.webapp.helpers.validation.implementations;


import ar.edu.itba.paw.interfaces.service.ClinicService;
import ar.edu.itba.paw.webapp.helpers.validation.annotations.UniqueClinic;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ClinicValidator implements ConstraintValidator<UniqueClinic,Object> {

    private String name;

    private String address;

    private String location;

    @Autowired
    ClinicService clinicService;

    public void initialize(UniqueClinic constraintAnnotation){
        this.name = constraintAnnotation.name();
        this.address = constraintAnnotation.address();
        this.location = constraintAnnotation.location();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context){
        Object nameValue = new BeanWrapperImpl(value).getPropertyValue(name);
        Object addressValue = new BeanWrapperImpl(value).getPropertyValue(address);
        Object locationValue = new BeanWrapperImpl(value).getPropertyValue(location);

        boolean valid = !clinicService.clinicExists((String) nameValue,(String) addressValue,(String) locationValue);
        if ( !valid ) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate( "{value.registered}")
                    .addNode(location).addConstraintViolation();
        }
        return valid;
    }
}
