package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.helpers.validation.annotations.ClinicExists;
import ar.edu.itba.paw.webapp.helpers.validation.annotations.Exists;
import ar.edu.itba.paw.webapp.helpers.validation.annotations.ValidAppointment;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@ValidAppointment(license = "license", clinic = "clinic", patient = "patient", time = "time", day = "day", month = "month", year = "year", message = "value.registered")
public class AppointmentForm {

    @Exists(field = "doctor", message = "value.not.exists")
    private String license;

    @ClinicExists(message = "value.not.exists")
    private int clinic;

    @Exists(field = "user", message = "value.not.exists")
    private String patient;

    //TODO valores
    @Min(value = 9, message = "schedule.min.hour.constraint")
    @Max(value = 18, message = "schedule.max.hour.constraint")
    private int time;

    @Min(value = 1, message = "schedule.min.day.constraint")
    @Max(value = 7, message = "schedule.max.day.constraint")
    private int day;

    @Min(value = 1, message = "appointment.min.month.constraint")
    @Max(value = 12, message = "appointment.max.month.constraint")
    private int month;

    @Min(value = 2021, message = "appointment.min.year.constraint")
    @Max(value = 202, message = "appointment.max.year.constraint")
    private int year;

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public int getClinic() {
        return clinic;
    }

    public void setClinic(int clinic) {
        this.clinic = clinic;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
