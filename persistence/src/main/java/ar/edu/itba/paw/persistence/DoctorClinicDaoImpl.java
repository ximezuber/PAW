package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.DoctorClinicDao;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.DoctorClinicKey;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class DoctorClinicDaoImpl implements DoctorClinicDao {

    @PersistenceContext
    private EntityManager entityManager;

    private static final int MAX_DOCTORS_CLINICS_PER_PAGE = 6;

    @Override
    public DoctorClinic createDoctorClinic(Doctor doctor, Clinic clinic, int consultPrice) {
        DoctorClinic doctorClinic = new DoctorClinic(doctor, clinic, consultPrice);
        entityManager.persist(doctorClinic);
        return doctorClinic;
    }

    @Override
    public List<DoctorClinic> getDoctorsSubscribedClinics(Doctor doctor) {
        TypedQuery<DoctorClinic> query = entityManager.createQuery("from DoctorClinic as dc "  +
                                         " where dc.doctor.license = :doctorLicense", DoctorClinic.class);
        query.setParameter("doctorLicense", doctor.getLicense());
        return query.getResultList();
    }

    @Override
    public DoctorClinic getDoctorClinic(String license, int clinic){
        return entityManager.find(DoctorClinic.class, new DoctorClinicKey(license,clinic));
    }

    @Override
    public List<DoctorClinic> getFilteredDoctors(final Location location, final Specialty specialty,
                                                 final String firstName, final String lastName, final Prepaid prepaid,
                                                 final int consultPrice){
        DoctorQueryBuilder builder = new DoctorQueryBuilder();
        builder.buildQuery(location.getLocationName(), specialty.getSpecialtyName(), firstName, lastName,
                prepaid.getName(), consultPrice);
        TypedQuery<DoctorClinic> query = entityManager.createQuery(builder.getQuery(), DoctorClinic.class);
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
        return query.getResultList();
    }

    @Override
    public List<DoctorClinic> getDoctorClinicPaginatedByList(Doctor doctor, int page) {
        TypedQuery<DoctorClinic> query = entityManager.createQuery("from DoctorClinic as dc "  +
                " where dc.doctor.license = :doctorLicense ORDER BY dc.doctor.user.lastName, dc.doctor.user.firstName",
                DoctorClinic.class);
        query.setParameter("doctorLicense", doctor.getLicense());
        return query
                .setFirstResult(page * MAX_DOCTORS_CLINICS_PER_PAGE)
                .setMaxResults(MAX_DOCTORS_CLINICS_PER_PAGE)
                .getResultList();
    }

    @Override
    public int maxPageAvailable() {
        return MAX_DOCTORS_CLINICS_PER_PAGE;
    }

    @Override
    public void editPrice(DoctorClinic dc, int price) {
        Query query = entityManager.createQuery("update DoctorClinic dc set dc.consultPrice = :newPrice " +
                "where dc.clinic.id = :id and dc.doctor.license = :license");
        query.setParameter("newPrice", price);
        query.setParameter("id", dc.getClinic().getId());
        query.setParameter("license", dc.getDoctor().getLicense());
        query.executeUpdate();
    }

    @Override
   public long deleteDoctorClinic(String license, int clinicid) {
        Query query = entityManager.createQuery("delete from DoctorClinic as docCli where " +
                "docCli.doctor.license = :license and docCli.clinic.id = :id");
        query.setParameter("license",license);
        query.setParameter("id",clinicid);
        return query.executeUpdate();
    }

}


