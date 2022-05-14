package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Appointment;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class AppointmentDto {

    private int year;
    private int month;
    private int day;
    private int hour;
    private int dayOfWeek;
    private URI patient;
    private URI doctor;
    private URI clinic;

    public static AppointmentDto fromAppointment(Appointment appointment, UriInfo uriInfo) {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.year = appointment.getAppointmentKey().getDate().getYear();
        appointmentDto.month = appointment.getAppointmentKey().getDate().getMonthValue();
        appointmentDto.day = appointment.getAppointmentKey().getDate().getDayOfMonth();
        appointmentDto.hour = appointment.getAppointmentKey().getDate().getHour();
        appointmentDto.dayOfWeek = appointment.getAppointmentKey().getDate().getDayOfWeek().getValue();
        appointmentDto.patient = appointment.getPatientUser() == null ? null : uriInfo.getBaseUriBuilder().path("patients")
                .path(appointment.getPatientUser().getEmail()).build();
        appointmentDto.doctor = uriInfo.getBaseUriBuilder().path("doctors")
                .path(appointment.getDoctorClinic().getDoctor().getLicense()).build();
        appointmentDto.clinic = uriInfo.getBaseUriBuilder().path("clinics")
                .path(String.valueOf(appointment.getDoctorClinic().getClinic().getId())).build();
        return appointmentDto;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public URI getPatient() {
        return patient;
    }

    public void setPatient(URI patient) {
        this.patient = patient;
    }

    public URI getDoctor() {
        return doctor;
    }

    public void setDoctor(URI doctor) {
        this.doctor = doctor;
    }

    public URI getClinic() {
        return clinic;
    }

    public void setClinic(URI clinic) {
        this.clinic = clinic;
    }
}
