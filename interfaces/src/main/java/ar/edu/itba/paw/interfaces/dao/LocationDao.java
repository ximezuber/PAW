package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Location;

import java.util.List;
import java.util.Optional;

public interface LocationDao extends PaginationDao<Location> {
    Location createLocation(String name);

    Optional<Location> getLocationByName(String locationName);

    List<Location> getLocations();

    void deleteLocation(Location location);
}
