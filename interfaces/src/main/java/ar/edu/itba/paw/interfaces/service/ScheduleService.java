package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.model.Doctor;
import ar.edu.itba.paw.model.DoctorClinic;
import ar.edu.itba.paw.model.Schedule;
import ar.edu.itba.paw.model.exceptions.ConflictException;
import ar.edu.itba.paw.model.exceptions.EntityNotFoundException;
import ar.edu.itba.paw.model.exceptions.OutOfRangeException;

import java.util.List;

public interface ScheduleService {
    Schedule createSchedule(int hour, int day, String license, int clinicId) throws ConflictException;

    List<Schedule> getDoctorClinicSchedule(DoctorClinic doctorClinic);

    List<Schedule> getDoctorSchedule (Doctor doctor);

    boolean doctorHasScheduleInClinic(DoctorClinic doctorClinic, int day, int hour);

    void deleteSchedule(int hour, int day, String license, int clinicId) throws OutOfRangeException, EntityNotFoundException;
}
