package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.AppointmentDao;
import ar.edu.itba.paw.model.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class AppointmentDaoImpl implements AppointmentDao {

    private final static int MAX_APPOINTMENTS_PER_PAGE = 6;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Appointment createAppointment(DoctorClinic doctorClinic, User patient, LocalDateTime date) {
        DoctorClinic dc = entityManager.find(DoctorClinic.class,
                                            new DoctorClinicKey(doctorClinic.getDoctor().getLicense(),
                                            doctorClinic.getClinic().getId()));
        User pat = entityManager.find(User.class, patient.getEmail());
        pat = entityManager.merge(pat);
        dc = entityManager.merge(dc);
        Appointment appointment = new Appointment(date, dc, pat);
        entityManager.persist(appointment);
        return appointment;
    }

    @Override
    public List<Appointment> getAllDoctorsAppointments(Doctor doctor) {
        TypedQuery<Appointment> query = entityManager.createQuery("FROM Appointment AS ap" +
                " WHERE ap.doctorClinic.doctor.license = :doctor", Appointment.class);
        query.setParameter("doctor", doctor.getLicense());
        return query.getResultList();
    }

    @Override
    public List<Appointment> getPaginatedAppointments(int page, Doctor doctor) {
        TypedQuery<Appointment> query = entityManager.createQuery("FROM Appointment AS ap" +
                " WEHRE ap.doctorClinic.doctor.license = :doctor ORDER BY ap.appointmentKey.date", Appointment.class);
        query.setParameter("doctor", doctor.getLicense());
        return query
                .setFirstResult(page * MAX_APPOINTMENTS_PER_PAGE)
                .setMaxResults(MAX_APPOINTMENTS_PER_PAGE)
                .getResultList();
    }

    @Override
    public List<Appointment> getPaginatedAppointments(int page, Patient patient) {
        TypedQuery<Appointment> query = entityManager.createQuery("FROM Appointment AS ap " +
                "WHERE ap.patient = :email ORDER BY ap.appointmentKey.date",
                Appointment.class);
        query.setParameter("email", patient.getEmail());
        return query
                .setFirstResult(page * MAX_APPOINTMENTS_PER_PAGE)
                .setMaxResults(MAX_APPOINTMENTS_PER_PAGE)
                .getResultList();
    }

    @Override
    public Optional<Appointment> getAppointment(Doctor doctor, LocalDateTime date) {
        Appointment appointment = entityManager.find(Appointment.class, new AppointmentKey(doctor.getLicense(), date));
        return Optional.ofNullable(appointment);
    }

    @Override
    public int getMaxAvailablePage(Patient patient) {
        return (int) (Math.ceil(( ((double)getPatientsAppointments(patient.getUser()).size()) / (double)MAX_APPOINTMENTS_PER_PAGE)));
    }

    @Override
    public int getMaxAvailablePage(Doctor doctor) {
        return (int) (Math.ceil(( ((double)getAllDoctorsAppointments(doctor).size()) / (double)MAX_APPOINTMENTS_PER_PAGE)));
    }

    @Transactional
    @Override
    public void cancelAppointment(Appointment appointment) {
        entityManager.remove(appointment);
    }

    @Override
    public int cancelAllAppointmentsOnSchedule(DoctorClinic doctorClinic, int day, int hour) {
        final Query query = entityManager.createQuery("DELETE FROM Appointment AS ap WHERE " +
                "ap.appointmentKey.doctor = :doctor AND ap.clinic = :clinic AND " +
                "DAY(ap.appointmentKey.date) = :day AND HOUR(ap.appointmentKey.date) = :hour");
        query.setParameter("doctor", doctorClinic.getDoctor().getLicense());
        query.setParameter("clinic", doctorClinic.getClinic().getId());
        query.setParameter("day", day - 1);
        query.setParameter("hour", hour);
        return query.executeUpdate();
    }

    @Override
    public boolean hasAppointment(String doctorLicense, String patientEmail, LocalDateTime date) {
        TypedQuery<Appointment> query = entityManager.createQuery("FROM Appointment AS ap" +
                " WHERE ap.doctorClinic.doctor.license = :doctor AND ap.patient = :email " +
                "AND ap.appointmentKey.date = :date", Appointment.class);
        query.setParameter("doctor", doctorLicense);
        query.setParameter("email", patientEmail);
        query.setParameter("date", date);
        return !query.getResultList().isEmpty();
    }

    @Override
    public boolean hasAppointment(Doctor doctor, LocalDateTime date) {
        TypedQuery<Appointment> query = entityManager.createQuery("FROM Appointment AS ap" +
                " WHERE ap.doctorClinic.doctor.license = :doctor " +
                "AND ap.appointmentKey.date = :date", Appointment.class);
        query.setParameter("doctor", doctor.getLicense());
        query.setParameter("date", date);
        return !query.getResultList().isEmpty();
    }

    @Override
    public boolean hasAppointment(User patient, LocalDateTime date) {
        TypedQuery<Appointment> query = entityManager.createQuery("FROM Appointment AS ap" +
                " WHERE ap.patient = :email " +
                "AND ap.appointmentKey.date = :date", Appointment.class);
        query.setParameter("email", patient.getEmail());
        query.setParameter("date", date);
        return !query.getResultList().isEmpty();
    }

    private List<Appointment> getPatientsAppointments(User patient) {
        TypedQuery<Appointment> query = entityManager.createQuery("FROM Appointment AS ap " +
                "WHERE ap.patient = :email", Appointment.class);
        query.setParameter("email", patient.getEmail());
        return query.getResultList();
    }
}
