package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.helpers.validation.annotations.UniqueSchedule;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@UniqueSchedule(license = "license", clinic = "clinic", day = "day", hour = "hour", message = "schedule.registered")
public class ScheduleForm {
    @Min(value = 1, message = "schedule.min.day.constraint")
    @Max(value = 7, message = "schedule.max.day.constraint")
    private int day;

    //TODO valores
    @Min(value = 9, message = "schedule.min.hour.constraint")
    @Max(value = 18, message = "schedule.max.hour.constraint")
    private int hour;

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
}
