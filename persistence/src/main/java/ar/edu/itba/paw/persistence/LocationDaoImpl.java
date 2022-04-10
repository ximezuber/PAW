package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.LocationDao;
import ar.edu.itba.paw.model.Location;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class LocationDaoImpl implements LocationDao {

    @PersistenceContext
    private EntityManager entityManager;

    private final static int MAX_LOCATIONS_PER_PAGE = 12;

    @Override
    public Optional<Location> getLocationByName(String locationName) {
        Location location = entityManager.find(Location.class, locationName);
        return Optional.ofNullable(location);
    }

    @Override
    public Location createLocation(String name){
        Location location = new Location(name);
        entityManager.persist(location);
        return location;
    }

    @Override
    public List<Location> getLocations() {
        TypedQuery<Location> query = entityManager.createQuery("FROM Location AS location ORDER BY location.name",
                Location.class);
        return query.getResultList();
    }

    @Override
    public List<Location> getPaginatedObjects(int page){
        TypedQuery<Location> query = entityManager.createQuery("FROM Location AS location " +
                "ORDER BY location.name", Location.class);

        return query.setFirstResult(page * MAX_LOCATIONS_PER_PAGE)
                                   .setMaxResults(MAX_LOCATIONS_PER_PAGE)
                                   .getResultList();
    }

    @Override
    public int maxAvailablePage() {
        return (int) (Math.ceil(( ((double) getLocations().size()) / (double) MAX_LOCATIONS_PER_PAGE)));
    }

    @Override
    public void deleteLocation(Location location) {
        Location contextLocation = entityManager.merge(location);
        entityManager.remove(contextLocation);
    }
}
