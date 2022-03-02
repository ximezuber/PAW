package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {

    LocalDateTime createAppointmentCalendar(int year, int month, int day, int time);

    void cancelAppointment(String license, int clinicId, int year, int month, int day, int time, boolean cancelledByDoctor)
            throws EntityNotFoundException, RequestEntityNotFoundException;

    void cancelUserAppointment(String userEmail, String license, int clinicId, int year, int month, int day, int time)
            throws EntityNotFoundException, RequestEntityNotFoundException;

    Appointment createAppointment(String license, int clinicId, String patientEmail, int year, int month, int day, int time)
            throws DateInPastException, AppointmentAlreadyScheduledException, OutOfScheduleException, HasAppointmentException;

    List<Appointment> getDoctorsAppointments(DoctorClinic doctorClinic);

    List<Appointment> getPatientsAppointments(User patient);

    List<Appointment> getPatientsAppointments(User patient, int clinicId);

    List<Appointment> getUserAppointments(User user);

    List<Appointment> getUserAppointmentsForClinic(User user, Clinic clinic);

    void cancelAppointment(String license, int clinicId, String userEmail, int year, int month, int day, int time, boolean cancelledByDoctor);

    Appointment hasAppointment(DoctorClinic doctorClinic, LocalDateTime date);

    boolean hasAppointment(String doctorLicense,String patientEmail, LocalDateTime date);

    boolean hasAppointment(Doctor doctor, LocalDateTime date);

    boolean hasAppointment(User patient, LocalDateTime date);

    List<Appointment> getAllDoctorsAppointments(Doctor doctor);

    List<Appointment> getDoctorAppointmentsWithinWeek(Doctor doctor, LocalDate beginning, LocalDate end);

    void cancelAllAppointmentsOnSchedule(DoctorClinic doctorClinic, int day, int hour);

    List<Appointment> getDoctorsAvailableAppointments(Doctor doctor);

    List<Appointment> getPaginatedAppointments(User user, int page);

    int getMaxAvailablePage(User user);
}
