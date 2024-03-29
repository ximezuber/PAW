package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentDao {

    Appointment createAppointment(DoctorClinic doctorClinic, User patient, LocalDateTime date);;

    List<Appointment> getAllDoctorsAppointments(Doctor doctor);

    List<Appointment> getPaginatedAppointments(int page, Doctor doctor);

    List<Appointment> getPaginatedAppointments(int page, Patient patient);

    Optional<Appointment> getAppointment(Doctor doctor, LocalDateTime date);

    Optional<Appointment> getAppointment(User patient, LocalDateTime date);

    int getMaxAvailablePage(Patient patient);

    int getMaxAvailablePage(Doctor doctor);

    void cancelAppointment(Appointment appointment);

    int cancelAllAppointmentsOnSchedule(DoctorClinic doctorClinic, int day, int hour);

    Optional<Appointment> getAppointment(Doctor doctor, Patient patient, LocalDateTime date);

}