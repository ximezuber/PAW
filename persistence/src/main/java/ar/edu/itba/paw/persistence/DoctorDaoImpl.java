package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.DoctorDao;
import ar.edu.itba.paw.model.Doctor;
import ar.edu.itba.paw.model.Specialty;
import ar.edu.itba.paw.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class DoctorDaoImpl implements DoctorDao {

    @PersistenceContext
    private EntityManager entityManager;

    private static final int MAX_DOCTORS_PER_PAGE_ADMIN = 9;
    private static final int MAX_DOCTORS_PER_PAGE_USER = 9;

    @Override
    public Doctor createDoctor(final Specialty specialty, final String license, final String phoneNumber, User user) {
        final Doctor doctor = new Doctor(specialty,license,phoneNumber,user);
        entityManager.persist(doctor);
        return doctor;
    }

    @Override
    public List<Doctor> getDoctors() {
        final TypedQuery<Doctor> query = entityManager.createQuery("from Doctor as doctor order by " +
                "doctor.user.firstName, doctor.user.lastName, doctor.license", Doctor.class);
        return query.getResultList();
    }

    @Override
    public List<Doctor> getPaginatedObjects(int page) {
        final TypedQuery<Doctor> query = entityManager.createQuery("from Doctor as doctor" +
                "order by " +
                "doctor.user.firstName, doctor.user.lastName, doctor.license",Doctor.class);

        return query.setFirstResult(page * MAX_DOCTORS_PER_PAGE_ADMIN)
                .setMaxResults(MAX_DOCTORS_PER_PAGE_ADMIN)
                .getResultList();

    }

    @Override
    public int maxAvailablePage() {
        return (int) (Math.ceil((((double)getDoctors().size()) / (double)MAX_DOCTORS_PER_PAGE_ADMIN)));
    }

    @Override
    public Doctor getDoctorByLicense(String license) {
        return entityManager.find(Doctor.class, license);
    }

    @Override
    public List<Doctor> getDoctorBySpecialty(Specialty specialty){
        final TypedQuery<Doctor> query = entityManager.createQuery("from Doctor as doctor " +
                "where doctor.specialty.name = :specialty",Doctor.class);

        query.setParameter("specialty",specialty.getSpecialtyName());
        return query.getResultList();
    }

    @Override
    public boolean isDoctor(String email) {
        final TypedQuery<Doctor> query = entityManager.createQuery("from Doctor as doctor" +
                " where doctor.user.email = :email",Doctor.class);
        query.setParameter("email",email);
        final List<Doctor> list = query.getResultList();
        return !list.isEmpty();
    }

    @Override
    public Doctor getDoctorByEmail(String email){
        final TypedQuery<Doctor> query = entityManager.createQuery("select doctor from Doctor as doctor  " +
                "where doctor.user.email = :email",Doctor.class);
        query.setParameter("email",email);
        final List<Doctor> list = query.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public void updateDoctor(String license, Map<String, String> args){
        final Query query = entityManager.createQuery("update Doctor doctor set doctor.phoneNumber = :phoneNumber," +
                " doctor.specialty.name = :specialty where doctor.license = :license");
        query.setParameter("license",license);
        query.setParameter("specialty",args.get("specialty"));
        query.setParameter("phoneNumber",args.get("phoneNumber"));
        query.executeUpdate();
    }

    @Override
    public List<Doctor> getPaginatedDoctorsInList(List<String> licenses, int page) {
        final TypedQuery<Doctor> query = entityManager.createQuery("from Doctor as doctor where doctor.license " +
                "IN (:filteredLicenses) order by " +
                "doctor.user.firstName, doctor.user.lastName, doctor.license",Doctor.class);
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
