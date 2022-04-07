package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Schedule;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class ScheduleDto {

    private int day;

    private int hour;

    private URI clinic;

    private URI doctor;

    public static ScheduleDto fromSchedule(Schedule schedule, UriInfo uriInfo) {
        ScheduleDto scheduleDto = new ScheduleDto();
        scheduleDto.day = schedule.getDay();
        scheduleDto.hour = schedule.getHour();
        scheduleDto.clinic = uriInfo.getBaseUriBuilder().path("clinics")
                .path(String.valueOf(schedule.getDoctorClinic().getClinic().getId())).build();
        scheduleDto.doctor = uriInfo.getBaseUriBuilder().path("doctors")
                .path(schedule.getDoctorClinic().getDoctor().getLicense()).build();
        return scheduleDto;
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

    public URI getClinic() {
        return clinic;
    }

    public void setClinic(URI clinic) {
        this.clinic = clinic;
    }

    public URI getDoctor() {
        return doctor;
    }

    public void setDoctor(URI doctor) {
        this.doctor = doctor;
    }
}
