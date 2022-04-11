package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Specialty;
import ar.edu.itba.paw.interfaces.dao.SpecialtyDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class SpecialtyDaoImpl implements SpecialtyDao {

    @PersistenceContext
    private EntityManager entityManager;

    private final static int MAX_SPECIALTIES_PER_PAGE = 12;

    @Override
    public Specialty createSpecialty(String name) {
        Specialty specialty = new Specialty(name);
        entityManager.persist(specialty);
        return specialty;
    }

    @Override
    public Optional<Specialty> getSpecialtyByName(String specialtyName) {
        Specialty specialty = entityManager.find(Specialty.class, specialtyName);
        return Optional.ofNullable(specialty);
    }

    @Override
    public List<Specialty> getSpecialties() {
        final TypedQuery<Specialty> query = entityManager.createQuery("FROM Specialty AS spcecialty" +
                " ORDER BY spcecialty.name", Specialty.class);
        return query.getResultList();
    }

    @Override
    public List<Specialty> getPaginatedObjects(int page){
        final TypedQuery<Specialty> query = entityManager.createQuery("FROM Specialty AS specialty " +
                "ORDER BY specialty.name", Specialty.class);

        return query.setFirstResult(page * MAX_SPECIALTIES_PER_PAGE)
                    .setMaxResults(MAX_SPECIALTIES_PER_PAGE)
                    .getResultList();
    }

    @Override
    public int maxAvailablePage() {
        return (int) (Math.ceil(( ((double) getSpecialties().size()) / (double) MAX_SPECIALTIES_PER_PAGE)));
    }

    @Override
    public void deleteSpecialty(Specialty specialty) {
        Specialty contextSpecialty = entityManager.merge(specialty);
        entityManager.remove(contextSpecialty);
    }
}
