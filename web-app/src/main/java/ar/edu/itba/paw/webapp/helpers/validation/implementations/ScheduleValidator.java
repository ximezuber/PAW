package ar.edu.itba.paw.webapp.helpers.validation.implementations;

import ar.edu.itba.paw.interfaces.service.ClinicService;
import ar.edu.itba.paw.interfaces.service.DoctorClinicService;
import ar.edu.itba.paw.interfaces.service.DoctorService;
import ar.edu.itba.paw.interfaces.service.ScheduleService;
import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.Doctor;
import ar.edu.itba.paw.model.DoctorClinic;
import ar.edu.itba.paw.webapp.helpers.validation.annotations.UniqueSchedule;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class ScheduleValidator implements ConstraintValidator<UniqueSchedule,Object> {

    private String license;

    private String clinic;

    private String hour;

    private String day;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    DoctorClinicService doctorClinicService;

    @Autowired
    DoctorService doctorService;

    @Autowired
    ClinicService clinicService;

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
        Optional<Doctor> doctor = doctorService.getDoctorByLicense(scheduleLicense.toString());
        Optional<Clinic> clinic = clinicService.getClinicById((int)scheduleClinic);
        if (clinic.isPresent() && doctor.isPresent()) {
            Optional<DoctorClinic> doctorClinic = doctorClinicService.getDoctorClinic(doctor.get(), clinic.get());
            if (doctorClinic.isPresent())
                return scheduleService.getDoctorsClinicSchedule(doctorClinic.get(), (int)scheduleDay, (int)scheduleHour)
                        .isPresent();
        }
        return false;
    }
}
