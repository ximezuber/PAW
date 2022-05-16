package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.DoctorDao;
import ar.edu.itba.paw.model.Doctor;
import ar.edu.itba.paw.model.Specialty;
import ar.edu.itba.paw.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class DoctorDaoImpl implements DoctorDao {

    @PersistenceContext
    private EntityManager entityManager;

    private static final int MAX_DOCTORS_PER_PAGE_ADMIN = 9;
    private static final int MAX_DOCTORS_PER_PAGE_USER = 9;

    @Override
    public Doctor createDoctor(Specialty specialty, String license, String phoneNumber, User user) {
        user = entityManager.merge(user);
        specialty = entityManager.merge(specialty);
        final Doctor doctor = new Doctor(specialty, license, phoneNumber, user);
        entityManager.persist(doctor);
        return doctor;
    }

    @Override
    public List<Doctor> getDoctors() {
        final TypedQuery<Doctor> query = entityManager.createQuery("FROM Doctor AS doctor ORDER BY " +
                "doctor.user.firstName, doctor.user.lastName, doctor.license", Doctor.class);
        return query.getResultList();
    }

    @Override
    public List<Doctor> getPaginatedObjects(int page) {
        final TypedQuery<Doctor> query = entityManager.createQuery("FROM Doctor AS doctor" +
                " ORDER BY doctor.user.lastName, doctor.user.firstName, doctor.license",
                Doctor.class);

        return query.setFirstResult(page * MAX_DOCTORS_PER_PAGE_ADMIN)
                .setMaxResults(MAX_DOCTORS_PER_PAGE_ADMIN)
                .getResultList();

    }

    @Override
    public int maxAvailablePage() {
        return (int) (Math.ceil((((double)getDoctors().size()) / (double)MAX_DOCTORS_PER_PAGE_ADMIN)));
    }

    @Override
    public Optional<Doctor> getDoctorByLicense(String license) {
        Doctor doctor = entityManager.find(Doctor.class, license);
        return Optional.ofNullable(doctor);
    }

    @Override
    public List<Doctor> getDoctorBySpecialty(Specialty specialty) {
        final TypedQuery<Doctor> query = entityManager.createQuery("FROM Doctor AS doctor " +
                "WHERE doctor.specialty.name = :specialty " +
                "ORDER BY doctor.user.firstName, doctor.user.lastName, doctor.license", Doctor.class);

        query.setParameter("specialty", specialty.getSpecialtyName());
        return query.getResultList();
    }

    @Override
    public Optional<Doctor> getDoctorByEmail(String email) {
        final TypedQuery<Doctor> query = entityManager.createQuery("FROM Doctor AS doctor  " +
                "WHERE doctor.user.email = :email", Doctor.class);
        query.setParameter("email", email);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public void updateDoctor(Doctor doctor, String license, Specialty specialty, String phoneNumber) {
        Doctor contextDoctor = entityManager.merge(doctor);
        specialty = entityManager.merge(specialty);
        contextDoctor.setLicense(license);
        contextDoctor.setSpecialty(specialty);
        contextDoctor.setPhoneNumber(phoneNumber);
        entityManager.persist(contextDoctor);
    }

    @Override
    public List<Doctor> getPaginatedDoctorsInList(List<String> licenses, int page) {
        final TypedQuery<Doctor> query = entityManager.createQuery("FROM Doctor AS doctor WHERE doctor.license " +
                "IN (:filteredLicenses) ORDER BY " +
                "doctor.user.firstName, doctor.user.lastName, doctor.license", Doctor.class);
        query.setParameter("filteredLicenses", licenses);

        return query
                .setFirstResult(page * MAX_DOCTORS_PER_PAGE_USER)
                .setMaxResults(MAX_DOCTORS_PER_PAGE_USER)
                .getResultList();
    }

    @Override
    public int maxAvailableDoctorsInListPage(List<String> licenses) {
        return (int) (Math.ceil(( ((double)licenses.size()) / (double)MAX_DOCTORS_PER_PAGE_USER)));
    }
}
