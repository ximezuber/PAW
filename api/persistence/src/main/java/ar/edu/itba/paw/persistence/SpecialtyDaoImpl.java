package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Specialty;
import ar.edu.itba.paw.interfaces.dao.SpecialtyDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

@Repository
public class SpecialtyDaoImpl implements SpecialtyDao {

    @PersistenceContext
    private EntityManager entityManager;

    private final static int MAX_SPECIALTIES_PER_PAGE = 12;

    @Override
    public Specialty createSpecialty(String name){
        Specialty specialty = new Specialty(name);
        entityManager.persist(specialty);
        return specialty;
    }

    @Override
    public Specialty getSpecialtyByName(String specialtyName){
        return entityManager.find(Specialty.class,specialtyName);
    }

    @Override
    public List<Specialty> getSpecialties(){
        final TypedQuery<Specialty> query = entityManager.createQuery("from Specialty as spcecialty ORDER BY spcecialty.name",Specialty.class);
        final List<Specialty> list = query.getResultList();
        return list;
    }

    @Override
    public List<Specialty> getPaginatedObjects(int page){

        Query nativeQuery = entityManager.createNativeQuery("SELECT name FROM specialties ORDER BY name");

        @SuppressWarnings("unchecked")
        List<String> ids = nativeQuery.setFirstResult(page * MAX_SPECIALTIES_PER_PAGE)
                .setMaxResults(MAX_SPECIALTIES_PER_PAGE)
                .getResultList();

        if(ids.isEmpty()) {
            return Collections.emptyList();
        }

        final TypedQuery<Specialty> query = entityManager.createQuery("from Specialty as specialty where specialty.name in (:filteredSpecialties) ORDER BY specialty.name",Specialty.class);
        query.setParameter("filteredSpecialties", ids);
        return query.getResultList();
    }

    @Override
    public int maxAvailablePage() {
        return (int) (Math.ceil(( ((double)getSpecialties().size()) / (double)MAX_SPECIALTIES_PER_PAGE)));
    }

    @Override
    public long deleteSpecialty(String name) {
        final Query query = entityManager.createQuery("delete from Specialty as specialty where specialty.name = :name");
        query.setParameter("name",name);
        return query.executeUpdate();
    }

    @Override
    public void updateSpecialty(String oldName, String name) {
        final Query query = entityManager.createQuery("update Specialty as sp set sp.name = :newName where sp.name = :oldName");
        query.setParameter("newName",name);
        query.setParameter("oldName", oldName);
        query.executeUpdate();
    }
}
