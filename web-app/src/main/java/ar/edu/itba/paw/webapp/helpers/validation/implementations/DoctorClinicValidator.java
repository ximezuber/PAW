package ar.edu.itba.paw.webapp.helpers.validation.implementations;


import ar.edu.itba.paw.interfaces.service.ClinicService;
import ar.edu.itba.paw.interfaces.service.DoctorClinicService;
import ar.edu.itba.paw.interfaces.service.DoctorService;
import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.Doctor;
import ar.edu.itba.paw.webapp.helpers.UserContextHelper;
import ar.edu.itba.paw.webapp.helpers.validation.annotations.UniqueDoctorClinic;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class DoctorClinicValidator implements ConstraintValidator<UniqueDoctorClinic,Object> {

    private String clinic;

    private Doctor doctor;

    @Autowired
    DoctorService doctorService;

    @Autowired
    ClinicService clinicService;

    @Autowired
    DoctorClinicService doctorClinicService;

    public void initialize(UniqueDoctorClinic constraintAnnotation){
        clinic = constraintAnnotation.clinic();
        doctor = doctorService.getDoctorByEmail(UserContextHelper.getLoggedUserEmail(SecurityContextHolder.getContext()))
                .orElse(null);
    }

    public boolean isValid(Object value, ConstraintValidatorContext context){

        Object clinicNumber = new BeanWrapperImpl(value).getPropertyValue(clinic);//clinicNumber es un int

        Optional<Clinic> clinic = clinicService.getClinicById( (int) clinicNumber);

        if (clinic.isPresent() && doctor != null) {
            boolean valid = !doctorClinicService.getDoctorClinic(doctor, clinic.get()).isPresent();

            if ( !valid ) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate( "{value.registered}")
                        .addNode(String.valueOf(clinic.get().getId())).addConstraintViolation();
            }
            return valid;
        }
        return false;
    }
}
