package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {

    LocalDateTime createAppointmentCalendar(int year, int month, int day, int time);

    Appointment createAppointment(String license, int clinicId, String patientEmail, int year, int month, int day, int time)
            throws DateInPastException, AppointmentAlreadyScheduledException, OutOfScheduleException, HasAppointmentException;

    List<Appointment> getDoctorsAppointments(DoctorClinic doctorClinic);

    List<Appointment> getPatientsAppointments(User patient);

    List<Appointment> getUserAppointmentsForClinic(User user, Clinic clinic);

    List<Appointment> getDoctorsAvailableAppointments(Doctor doctor);

    // TODO: See what to do with Pagination service interface
    List<Appointment> getPaginatedAppointments(User user, int page);

    int getMaxAvailablePage(User user);

    boolean hasAppointment(String doctorLicense,String patientEmail, LocalDateTime date);

    void cancelUserAppointment(String userEmail, String license, int clinicId, int year, int month, int day, int time)
            throws EntityNotFoundException, RequestEntityNotFoundException;

    int cancelAllAppointmentsOnSchedule(DoctorClinic doctorClinic, int day, int hour);
}
