package ar.edu.itba.paw.webapp.helpers.validation.implementations;

import ar.edu.itba.paw.interfaces.service.DoctorClinicService;
import ar.edu.itba.paw.interfaces.service.ScheduleService;
import ar.edu.itba.paw.model.DoctorClinic;
import ar.edu.itba.paw.webapp.helpers.validation.annotations.UniqueSchedule;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ScheduleValidator implements ConstraintValidator<UniqueSchedule,Object> {

    private String license;

    private String clinic;

    private String hour;

    private String day;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    DoctorClinicService doctorClinicService;

    @Override
    public void initialize(UniqueSchedule constraintAnnotation) {
        license = constraintAnnotation.license();
        clinic = constraintAnnotation.clinic();
        hour = constraintAnnotation.hour();
        day = constraintAnnotation.day();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object scheduleLicense = new BeanWrapperImpl(value).getPropertyValue(license);
        Object scheduleClinic = new BeanWrapperImpl(value).getPropertyValue(clinic);
        Object scheduleHour = new BeanWrapperImpl(value).getPropertyValue(hour);
        Object scheduleDay = new BeanWrapperImpl(value).getPropertyValue(day);
        DoctorClinic doctorClinic = doctorClinicService.getDoctorClinic(scheduleLicense.toString(), (int)scheduleClinic);
        return !scheduleService.doctorHasScheduleInClinic(doctorClinic, (int)scheduleDay, (int)scheduleHour);
    }
}
