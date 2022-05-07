package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.PrepaidDao;
import ar.edu.itba.paw.model.Prepaid;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class PrepaidDaoImpl implements PrepaidDao {

    @PersistenceContext
    private EntityManager entityManager;

    private final static int MAX_PREPAID_PER_PAGE = 12;

    @Override
    public Prepaid createPrepaid(String name) {
        Prepaid prepaid = new Prepaid(name);
        entityManager.persist(prepaid);
        return prepaid;
    }

    @Override
    public Optional<Prepaid> getPrepaidByName(String prepaidName) {
        Prepaid prepaid = entityManager.find(Prepaid.class, prepaidName);
        return Optional.ofNullable(prepaid);
    }

    @Override
    public List<Prepaid> getPrepaid() {
        TypedQuery<Prepaid> query = entityManager.createQuery("FROM Prepaid AS prepaid ORDER BY prepaid.name",
                Prepaid.class);
        return query.getResultList();
    }

    @Override
    public List<Prepaid> getPaginatedObjects(int page){
        TypedQuery<Prepaid> query = entityManager.createQuery("FROM Prepaid AS prepaid " +
                "ORDER BY prepaid.name", Prepaid.class);

        return query.setFirstResult(page * MAX_PREPAID_PER_PAGE)
                .setMaxResults(MAX_PREPAID_PER_PAGE)
                .getResultList();
    }

    @Override
    public int maxAvailablePage() {
        return (int) (Math.ceil(( ((double) getPrepaid().size()) / (double) MAX_PREPAID_PER_PAGE)));
    }

    @Override
    public void deletePrepaid(Prepaid prepaid) {
        Prepaid contextPrepaid = entityManager.merge(prepaid);
        entityManager.remove(contextPrepaid);
    }
}
