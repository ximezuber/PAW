package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.DoctorClinicDao;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.DoctorClinicKey;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class DoctorClinicDaoImpl implements DoctorClinicDao {

    @PersistenceContext
    private EntityManager entityManager;

    private static final int MAX_DOCTORS_CLINICS_PER_PAGE = 6;
    private static final int MAX_FILTERED_DOCTORS_CLINICS_PER_PAGE = 9;

    @Override
    public DoctorClinic createDoctorClinic(Doctor doctor, Clinic clinic, int consultPrice) {
        doctor = entityManager.merge(doctor);
        clinic = entityManager.merge(clinic);
        DoctorClinic doctorClinic = new DoctorClinic(doctor, clinic, consultPrice);
        entityManager.persist(doctorClinic);
        return doctorClinic;
    }

    @Override
    public List<DoctorClinic> getDoctorsSubscribedClinics(Doctor doctor) {
        TypedQuery<DoctorClinic> query = entityManager.createQuery("FROM DoctorClinic AS dc "  +
                                         " WHERE dc.doctor.license = :doctorLicense", DoctorClinic.class);
        query.setParameter("doctorLicense", doctor.getLicense());
        return query.getResultList();
    }

    @Override
    public Optional<DoctorClinic> getDoctorClinic(Doctor doctor, Clinic clinic){
        DoctorClinic dc = entityManager.find(DoctorClinic.class,
                new DoctorClinicKey(doctor.getLicense(), clinic.getId()));
        return Optional.ofNullable(dc);
    }

    @Override
    public List<Doctor> getFilteredDoctorInClinics(Location location, Specialty specialty, String firstName,
                                                   String lastName, Prepaid prepaid, int consultPrice) {
        return createQuery(location, specialty, firstName, lastName, prepaid, consultPrice)
                .getResultList();
    }

    @Override
    public List<Doctor> getFilteredDoctorInClinicsPaginated(final Location location, final Specialty specialty,
                                                            final String firstName, final String lastName, final Prepaid prepaid,
                                                            final int consultPrice, final int page) {
        return createQuery(location, specialty, firstName, lastName, prepaid, consultPrice)
                .setFirstResult(page * MAX_FILTERED_DOCTORS_CLINICS_PER_PAGE)
                .setMaxResults(MAX_FILTERED_DOCTORS_CLINICS_PER_PAGE)
                .getResultList();
    }

    @Override
    public List<DoctorClinic> getDoctorClinicPaginatedByList(Doctor doctor, int page) {
        TypedQuery<DoctorClinic> query = entityManager.createQuery("FROM DoctorClinic AS dc "  +
                " WHERE dc.doctor.license = :doctorLicense ORDER BY dc.doctor.user.lastName, dc.doctor.user.firstName",
                DoctorClinic.class);
        query.setParameter("doctorLicense", doctor.getLicense());
        return query
                .setFirstResult(page * MAX_DOCTORS_CLINICS_PER_PAGE)
                .setMaxResults(MAX_DOCTORS_CLINICS_PER_PAGE)
                .getResultList();
    }

    @Override
    public int maxAvailableFilteredDoctorClinicPage(Location location, Specialty specialty,
                                                    String firstName, String lastName, Prepaid prepaid,
                                                    int consultPrice) {
        return (int) (Math.ceil(( ((double) getFilteredDoctorInClinics(location, specialty,
                firstName, lastName, prepaid, consultPrice).size()) /
                (double) MAX_FILTERED_DOCTORS_CLINICS_PER_PAGE)));
    }

    @Override
    public int maxPageAvailable(Doctor doctor) {
        return (int) (Math.ceil(( ((double) getDoctorsSubscribedClinics(doctor).size()) /
                (double) MAX_DOCTORS_CLINICS_PER_PAGE)));
    }

    @Override
    public void editPrice(DoctorClinic dc, int price) {
        DoctorClinic contextDoctorClinic = entityManager.merge(dc);
        dc.setConsultPrice(price);
        entityManager.persist(contextDoctorClinic);
    }

    @Override
   public void deleteDoctorClinic(DoctorClinic dc) {
        DoctorClinic contextDoctorClinic = entityManager.merge(dc);
        entityManager.remove(contextDoctorClinic);
    }

    private TypedQuery<Doctor> createQuery(final Location location, final Specialty specialty,
                                                 final String firstName, final String lastName, final Prepaid prepaid,
                                                 final int consultPrice) {
        DoctorQueryBuilder builder = new DoctorQueryBuilder();
        builder.buildQuery(location.getLocationName(), specialty.getSpecialtyName(), firstName, lastName,
                prepaid.getName(), consultPrice);
        TypedQuery<Doctor> query = entityManager.createQuery(builder.getQuery(), Doctor.class);
        if(!location.getLocationName().equals("")) {
            query.setParameter("location", location.getLocationName());
        }
        if(!specialty.getSpecialtyName().equals("")) {
            query.setParameter("specialty", specialty.getSpecialtyName());
        }
        if(!firstName.equals("")) {
            query.setParameter("firstName", firstName);
        }
        if(!lastName.equals("")) {
            query.setParameter("lastName", lastName);
        }
        if(!prepaid.getName().equals("")) {
            query.setParameter("prepaid", prepaid.getName());
        }
        else if( consultPrice > 0) {
            query.setParameter("consultPrice",consultPrice);
        }
        return query;
    }
}


