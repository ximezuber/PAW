package ar.edu.itba.paw.webapp.helpers.validation.implementations;


import ar.edu.itba.paw.interfaces.service.DoctorClinicService;
import ar.edu.itba.paw.interfaces.service.DoctorService;
import ar.edu.itba.paw.webapp.helpers.UserContextHelper;
import ar.edu.itba.paw.webapp.helpers.validation.annotations.UniqueDoctorClinic;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DoctorClinicValidator implements ConstraintValidator<UniqueDoctorClinic,Object> {

    private String clinic;

    private String doctor;

    @Autowired
    DoctorService doctorService;

    @Autowired
    DoctorClinicService doctorClinicService;

    public void initialize(UniqueDoctorClinic constraintAnnotation){
        clinic = constraintAnnotation.clinic();
        doctor = doctorService.getDoctorByEmail(UserContextHelper.getLoggedUserEmail(SecurityContextHolder.getContext())).getLicense();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context){

        Object clinicNumber = new BeanWrapperImpl(value).getPropertyValue(clinic); //clinicNumber es un int

        boolean valid = doctorClinicService.getDoctorClinic(doctor,(int)clinicNumber) == null;

        if ( !valid ) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate( "{value.registered}")
                    .addNode(clinic).addConstraintViolation();
        }
        return valid;


    }
}
