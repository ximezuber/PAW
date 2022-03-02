package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Appointment;

import javax.ws.rs.core.UriInfo;
import java.time.LocalDateTime;

public class AppointmentDto {

    private UserDto patient;
    private LocalDateTime date;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int dayWeek;
    private DoctorClinicDto doctorClinic;

    public static AppointmentDto fromAppointment(Appointment appointment, UriInfo uriInfo) {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.patient = appointment.getPatientUser() == null ? null : UserDto.fromUser(appointment.getPatientUser());
        appointmentDto.date = appointment.getAppointmentKey().getDate();
        appointmentDto.year = appointment.getAppointmentKey().getDate().getYear();
        appointmentDto.month = appointment.getAppointmentKey().getDate().getMonthValue();
        appointmentDto.day = appointment.getAppointmentKey().getDate().getDayOfMonth();
        appointmentDto.hour = appointment.getAppointmentKey().getDate().getHour();
        appointmentDto.dayWeek = appointment.getAppointmentKey().getDate().getDayOfWeek().getValue();
        appointmentDto.doctorClinic = DoctorClinicDto.fromDoctorClinic(appointment.getDoctorClinic(), uriInfo);
        return appointmentDto;
    }

    public UserDto getPatient() {
        return patient;
    }

    public void setPatient(UserDto patient) {
        this.patient = patient;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public DoctorClinicDto getDoctorClinic() {
        return doctorClinic;
    }

    public void setDoctorClinic(DoctorClinicDto doctorClinic) {
        this.doctorClinic = doctorClinic;
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

    public int getDayWeek() {
        return dayWeek;
    }

    public void setDayWeek(int dayWeek) {
        this.dayWeek = dayWeek;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
