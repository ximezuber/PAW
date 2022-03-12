package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentDao {

    Appointment createAppointment(DoctorClinic doctorClinic, User patient, LocalDateTime date);

    List<Appointment> getDoctorsAppointments(DoctorClinic doctorClinic);

    List<Appointment> getPatientsAppointments(User patient);

    List<Appointment> getPatientsAppointments(User patient, int clinicId);

    List<Appointment> getAllDoctorsAppointments(Doctor doctor);

    List<Appointment> getPaginatedAppointments(int page, Doctor doctor);

    List<Appointment> getPaginatedAppointments(int page, Patient patient);

    int getMaxAvailablePage(Patient patient);

    int getMaxAvailablePage(Doctor doctor);

    int cancelAppointment(DoctorClinic doctorClinic, User patient, LocalDateTime date);

    int cancelAllAppointmentsOnSchedule(DoctorClinic doctorClinic, int day, int hour);

    Appointment hasAppointment(DoctorClinic doctorClinic, LocalDateTime date);

    boolean hasAppointment(String doctorLicense,String patientEmail, LocalDateTime date);

    boolean hasAppointment(Doctor doctor, LocalDateTime date);

    boolean hasAppointment(User patient, LocalDateTime date);

}