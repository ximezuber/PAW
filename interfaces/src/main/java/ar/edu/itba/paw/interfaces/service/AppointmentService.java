package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {

    LocalDateTime createAppointmentCalendar(int year, int month, int day, int time);

    /**
     * Creates an Appointment
     * @param license doctor's license
     * @param clinicId clinic's id
     * @param patientEmail patient's email
     * @param year when the appointment is
     * @param month when the appointment is
     * @param day when the appointment is
     * @param time when the appointment is
     * @return Appointment created
     * @throws DateInPastException Date of appointment that is tried to be used has already past
     * @throws AppointmentAlreadyScheduledException The appointment is a duplicate
     * @throws OutOfScheduleException The doctor is not scheduled at that clinic that time
     * @throws HasAppointmentException Either the patient or the doctor already has an appointment on that time
     */
    Appointment createAppointment(String license, int clinicId, String patientEmail, int year, int month, int day, int time)
            throws DateInPastException, AppointmentAlreadyScheduledException, OutOfScheduleException, HasAppointmentException;

    List<Appointment> getDoctorsAppointments(DoctorClinic doctorClinic);

    List<Appointment> getPatientsAppointments(User patient);

    List<Appointment> getDoctorsAvailableAppointments(Doctor doctor);

    // TODO: See what to do with Pagination service interface
    List<Appointment> getPaginatedAppointments(User user, int page);

    Optional<Appointment> getAppointment(String license, int year, int month, int day, int time) throws EntityNotFoundException;

    int getMaxAvailablePage(User user);

    boolean isAppointment(String doctorLicense, String patientEmail, LocalDateTime date);

    /**
     * Cancel an Appointment
     * @param userEmail email of the user that wants to cancel (patient or doctor)
     * @param license doctor's license
     * @param year when the appointment is
     * @param month when the appointment is
     * @param day when the appointment is
     * @param time when the appointment is
     * @throws EntityNotFoundException either the doctor in a clinic or patient is not found
     */
    void cancelUserAppointment(String userEmail, String license, int year, int month, int day, int time)
            throws EntityNotFoundException;

    int cancelAllAppointmentsOnSchedule(DoctorClinic doctorClinic, int day, int hour);
}
