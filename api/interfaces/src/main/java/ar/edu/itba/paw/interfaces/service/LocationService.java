package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.exceptions.DuplicateEntityException;
import ar.edu.itba.paw.model.exceptions.EntityDependencyException;
import ar.edu.itba.paw.model.exceptions.EntityNotFoundException;

import java.util.List;

public interface LocationService extends PaginationService<Location> {
    Location createLocation(String name) throws DuplicateEntityException;

    List<Location> getLocations();

    Location getLocationByName(String locationName);

    void updateLocation(String oldName, String name);

    long deleteLocation(String name) throws EntityNotFoundException, EntityDependencyException;
}
