package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.exceptions.DuplicateEntityException;
import ar.edu.itba.paw.model.exceptions.EntityDependencyException;

import java.util.List;
import java.util.Optional;

public interface LocationService extends PaginationService<Location> {
    Location createLocation(String name) throws DuplicateEntityException;

    List<Location> getLocations();

    Optional<Location> getLocationByName(String locationName);

    void deleteLocation(Location location) throws EntityDependencyException;
}
