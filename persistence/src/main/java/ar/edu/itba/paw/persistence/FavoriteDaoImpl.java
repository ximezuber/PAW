package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.FavoriteDao;
import ar.edu.itba.paw.model.Doctor;
import ar.edu.itba.paw.model.Favorite;
import ar.edu.itba.paw.model.FavoriteKey;
import ar.edu.itba.paw.model.Patient;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class FavoriteDaoImpl implements FavoriteDao {

    private final static int MAX_FAVORITES_PER_PAGE = 12;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Favorite create(Doctor doctor, Patient patient) {
        doctor = entityManager.merge(doctor);
        patient = entityManager.merge(patient);
        Favorite favorite = new Favorite(doctor, patient);
        entityManager.persist(favorite);
        return favorite;
    }

    @Override
    public List<Favorite> getPatientsFavorite(Patient patient) {
        final TypedQuery<Favorite> query = entityManager.createQuery("FROM Favorite AS fav " +
                "WHERE fav.favoriteKey.patient = :email", Favorite.class);
        query.setParameter("email", patient.getEmail());

        return query.getResultList();
    }

    @Override
    public Optional<Favorite> getFavorite(Doctor doctor, Patient patient) {
        Favorite favorite = entityManager.find(Favorite.class, new FavoriteKey(doctor.getLicense(),
                patient.getEmail()));
        return Optional.ofNullable(favorite);
    }

    @Override
    public void deleteFavorite(Favorite favorite) {
        Favorite contextFav = entityManager.merge(favorite);
        entityManager.remove(contextFav);
    }

    @Override
    public List<Favorite> getPaginatedObjects(int page, Patient patient) {
        final TypedQuery<Favorite> query = entityManager.createQuery("FROM Favorite AS fav " +
                "WHERE fav.favoriteKey.patient = :email", Favorite.class);
        query.setParameter("email", patient.getEmail());
        return query.setFirstResult(page * MAX_FAVORITES_PER_PAGE)
                .setMaxResults(MAX_FAVORITES_PER_PAGE)
                .getResultList();
    }

    @Override
    public int maxAvailablePage(Patient patient) {
        return (int) (Math.ceil(( ((double)getPatientsFavorite(patient).size()) / (double)MAX_FAVORITES_PER_PAGE)));
    }
}
