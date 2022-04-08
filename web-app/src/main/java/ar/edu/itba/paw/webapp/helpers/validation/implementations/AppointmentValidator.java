package ar.edu.itba.paw.webapp.helpers.validation.implementations;

import ar.edu.itba.paw.interfaces.service.AppointmentService;
import ar.edu.itba.paw.webapp.helpers.validation.annotations.ValidAppointment;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class AppointmentValidator implements ConstraintValidator<ValidAppointment, Object> {

    private String license;

    private String patient;

    private String clinic;

    private String time;

    private String day;

    private String month;

    private String year;

    @Autowired
    private AppointmentService appointmentService;

    @Override
    public void initialize(ValidAppointment constraintAnnotation) {
        license = constraintAnnotation.license();
        clinic = constraintAnnotation.clinic();
        patient = constraintAnnotation.patient();
        time = constraintAnnotation.time();
        day = constraintAnnotation.day();
        month = constraintAnnotation.month();
        year = constraintAnnotation.year();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object scheduleLicense = new BeanWrapperImpl(value).getPropertyValue(license);
        Object scheduleClinic = new BeanWrapperImpl(value).getPropertyValue(clinic);
        Object schedulePatient = new BeanWrapperImpl(value).getPropertyValue(patient);
        Object scheduleTime = new BeanWrapperImpl(value).getPropertyValue(time);
        Object scheduleDay = new BeanWrapperImpl(value).getPropertyValue(day);
        Object scheduleMonth = new BeanWrapperImpl(value).getPropertyValue(month);
        Object scheduleYear = new BeanWrapperImpl(value).getPropertyValue(year);

        final String DATE_FORMAT = "dd-MM-yyyy";
        String monthPad = (((int)scheduleMonth) < 10 ? "0" : "");
        String dayPad = (((int)scheduleDay) < 10 ? "0" : "");
        String date = dayPad + scheduleDay + "-" + monthPad + scheduleMonth + "-" + scheduleYear;
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            df.parse(date);
            LocalDateTime appointment = appointmentService.createAppointmentCalendar((int)scheduleYear, (int)scheduleMonth, (int)scheduleDay, (int)scheduleTime);
            return appointmentService.isAppointment(scheduleLicense.toString(), schedulePatient.toString(), appointment);
        } catch (ParseException e) {
            return false;
        }
    }
}
