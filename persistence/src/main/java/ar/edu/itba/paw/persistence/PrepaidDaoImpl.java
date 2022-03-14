package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.PrepaidDao;
import ar.edu.itba.paw.model.Prepaid;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

@Repository
public class PrepaidDaoImpl implements PrepaidDao {

    @PersistenceContext
    private EntityManager entityManager;

    private final static int MAX_PREPAIDS_PER_PAGE = 12;

    @Override
    public Prepaid createPrepaid(String name){
        Prepaid prepaid = new Prepaid(name);
        entityManager.persist(prepaid);
        return prepaid;
    }

    @Override
    public Prepaid getPrepaidByName(String prepaidName){
        return entityManager.find(Prepaid.class, prepaidName);
    }

    @Override
    public List<Prepaid> getPrepaids(){
        TypedQuery<Prepaid> query = entityManager.createQuery("from Prepaid as prepaid order by prepaid.name",
                Prepaid.class);
        List<Prepaid> list = query.getResultList();
        return list;
    }

    @Override
    public List<Prepaid> getPaginatedObjects(int page){

        TypedQuery<Prepaid> query = entityManager.createQuery("from Prepaid as prepaid " +
                "ORDER BY prepaid.name", Prepaid.class);

        return query.setFirstResult(page * MAX_PREPAIDS_PER_PAGE)
                .setMaxResults(MAX_PREPAIDS_PER_PAGE)
                .getResultList();
    }

    @Override
    public int maxAvailablePage() {
        return (int) (Math.ceil(( ((double)getPrepaids().size()) / (double)MAX_PREPAIDS_PER_PAGE)));
    }

    @Override
    public long deletePrepaid(String name){
        Query query = entityManager.createQuery("delete from Prepaid as prepaid where prepaid.name = :name");
        query.setParameter("name",name);
        return query.executeUpdate();
    }
}
