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

    /**
     * Returns a list of available appointments from the moment the consult is done to 9 weeks in time.
     * @param doctor
     * @return list of available Appointment
     */
    List<Appointment> getDoctorsAvailableAppointments(Doctor doctor);

    // TODO: See what to do with Pagination service interface

    /**
     * Returns list of paginated Appointment for a certain user
     * @param user doctor's or patient's user. The user making the query.
     * @param page
     * @return list of Appointments
     */
    List<Appointment> getPaginatedAppointments(User user, int page);

    /**
     * Returns a specific Appointment
     * @param license
     * @param year
     * @param month
     * @param day
     * @param time
     * @return Optional of Appointment
     * @throws EntityNotFoundException
     */
    Optional<Appointment> getAppointment(String license, int year, int month, int day, int time)
            throws EntityNotFoundException;

    /**
     * Returns the maximum available page for paginated appointments
     * @param user
     * @return maximum number of pages
     */
    int getMaxAvailablePage(User user);

    /**
     * Validate if appointment exists
     * @param doctorLicense
     * @param patientEmail
     * @param date
     * @return
     */
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

    /**
     * Cancels all Appointments that fall in a certain schedule
     * @param doctorClinic
     * @param day
     * @param hour
     * @return
     */
    int cancelAllAppointmentsOnSchedule(DoctorClinic doctorClinic, int day, int hour);
}
