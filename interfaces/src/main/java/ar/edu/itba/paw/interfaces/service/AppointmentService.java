package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {

    LocalDateTime createAppointmentCalendar(int year, int month, int day, int time);

    Appointment createAppointment(DoctorClinic doctorClinic, User patient, int year, int month, int day, int time)
            throws DateInPastException, AppointmentAlreadyScheduledException, OutOfScheduleException, HasAppointmentException;

    List<Appointment> getDoctorsAvailableAppointments(Doctor doctor);

    List<Appointment> getPaginatedAppointments(User user, int page) throws EntityNotFoundException;

    Optional<Appointment> getAppointment(Doctor doctor, int year, int month, int day, int time);

    Optional<Appointment> getAppointment(User patient, int year, int month, int day, int time)
            throws EntityNotFoundException;

    int getMaxAvailablePage(User user) throws EntityNotFoundException;

    Optional<Appointment> getAppointment(Doctor doctor, Patient patient, LocalDateTime date);

    void cancelUserAppointment(String userEmail, String license, int year, int month, int day, int time)
            throws EntityNotFoundException;

    int cancelAllAppointmentsOnSchedule(DoctorClinic doctorClinic, int day, int hour);

    boolean isAppointment(String license, String patient, LocalDateTime date);
}
