package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.ClinicDao;
import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.Location;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class ClinicDaoImpl implements ClinicDao {

    @PersistenceContext
    private EntityManager entityManager;

    private final static int MAX_CLINICS_PER_PAGE = 6;

    @Override
    public Clinic createClinic(String name, String address, Location location) {
        Clinic clinic = new Clinic();
        location = entityManager.merge(location);
        clinic.setLocation(location);
        clinic.setName(name);
        clinic.setAddress(address);
        entityManager.persist(clinic);
        return clinic;
    }

    @Override
    public List<Clinic> getClinics() {
        TypedQuery<Clinic> query = entityManager.createQuery("FROM Clinic AS clinic ORDER BY " +
                        "clinic.name, clinic.location.name, clinic.address", Clinic.class);
        return query.getResultList();
    }

    @Override
    public List<Clinic> getPaginatedObjects(int page) {
        TypedQuery<Clinic> query = entityManager.createQuery("FROM Clinic AS clinic " +
                "ORDER BY clinic.name, clinic.location.name, clinic.address", Clinic.class);
        return query.setFirstResult(page * MAX_CLINICS_PER_PAGE)
                .setMaxResults(MAX_CLINICS_PER_PAGE)
                .getResultList();
    }

    @Override
    public int maxAvailablePage() {
        return (int) (Math.ceil((((double)getClinics().size()) / (double) MAX_CLINICS_PER_PAGE)));
    }

    @Override
    public Optional<Clinic> getClinicById(int id) {
        Clinic clinic = entityManager.find(Clinic.class, id);
        return Optional.ofNullable(clinic);
    }

    @Override
    public List<Clinic> getClinicsByLocation(Location location) {
        TypedQuery<Clinic> query = entityManager.createQuery("FROM Clinic AS clinic WHERE" +
                        " clinic.location.name = :location", Clinic.class);
        query.setParameter("location", location.getLocationName());
        return query.getResultList();
    }
    @Override
    public boolean clinicExists(String name, String address, Location location) {
        TypedQuery<Clinic> query = entityManager.createQuery("FROM Clinic AS clinic" +
                " WHERE clinic.location.name = :location AND clinic.name = :name AND clinic.address = :address",
                Clinic.class);
        query.setParameter("location", location.getLocationName());
        query.setParameter("name", name);
        query.setParameter("address", address);
        return !query.getResultList().isEmpty();
    }

    @Override
    public void updateClinic(Clinic clinic, String name, String address, Location location) {
        Clinic contextClinic = entityManager.merge(clinic);
        location = entityManager.merge(location);
        contextClinic.setName(name);
        contextClinic.setAddress(address);
        contextClinic.setLocation(location);
        entityManager.persist(contextClinic);
    }


    @Override
    public void deleteClinic(Clinic clinic) {
        Clinic contextClinic = entityManager.merge(clinic);
        entityManager.remove(contextClinic);
    }
}
