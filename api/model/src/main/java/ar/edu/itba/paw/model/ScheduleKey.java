package ar.edu.itba.paw.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ScheduleKey implements Serializable {

    @Column
    private int day;

    @Column
    private int hour;

    @Column
    private String doctor;

    public ScheduleKey(int day, int hour, String doctor) {
        this.day = day;
        this.hour = hour;
        this.doctor = doctor;
    }

    public ScheduleKey(){}

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

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleKey that = (ScheduleKey) o;
        return getDay() == that.getDay() &&
                getHour() == that.getHour() &&
                Objects.equals(getDoctor(), that.getDoctor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDay(), getHour(), getDoctor());
    }
 }
