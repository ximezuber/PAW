package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Doctor;
import ar.edu.itba.paw.model.DoctorClinic;
import ar.edu.itba.paw.model.Schedule;

import java.util.List;

public interface ScheduleDao {
    Schedule createSchedule(int hour, int day, DoctorClinic doctorClinic);

    List<Schedule> getDoctorClinicSchedule(DoctorClinic doctorClinic);

    List<Schedule> getDoctorsSchedule(Doctor doctor);

    int deleteSchedule(int hour, int day, DoctorClinic doctorClinic);

    boolean doctorHasSchedule(Doctor doctor, int day, int hour);
}
