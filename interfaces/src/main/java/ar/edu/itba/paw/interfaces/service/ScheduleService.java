package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.model.Doctor;
import ar.edu.itba.paw.model.DoctorClinic;
import ar.edu.itba.paw.model.Schedule;
import ar.edu.itba.paw.model.exceptions.ConflictException;
import ar.edu.itba.paw.model.exceptions.EntityNotFoundException;
import ar.edu.itba.paw.model.exceptions.OutOfRangeException;

import java.util.List;
import java.util.Optional;

public interface ScheduleService {
    Schedule createSchedule(int hour, int day, DoctorClinic doctorClinic) throws ConflictException;

    List<Schedule> getDoctorClinicSchedule(DoctorClinic doctorClinic);

    List<Schedule> getDoctorSchedule(Doctor doctor);

    Optional<Schedule> getDoctorsClinicSchedule(DoctorClinic doctorClinic, int day, int hour);

    void deleteSchedule(Schedule schedule) throws OutOfRangeException, EntityNotFoundException;
}
