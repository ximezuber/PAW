package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Schedule;

import javax.ws.rs.core.UriInfo;

public class ScheduleDto {

    private int day;

    private int hour;

    private ClinicDto clinic;

    public static ScheduleDto fromSchedule(Schedule schedule, UriInfo uriInfo) {
        ScheduleDto scheduleDto = new ScheduleDto();
        scheduleDto.day = schedule.getDay();
        scheduleDto.hour = schedule.getHour();
        scheduleDto.clinic = ClinicDto.fromClinic(schedule.getDoctorClinic().getClinic(), uriInfo);
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

    public ClinicDto getClinic() {
        return clinic;
    }

    public void setClinic(ClinicDto clinic) {
        this.clinic = clinic;
    }
}
