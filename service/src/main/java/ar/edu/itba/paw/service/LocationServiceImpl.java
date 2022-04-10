package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.LocationDao;
import ar.edu.itba.paw.interfaces.service.ClinicService;
import ar.edu.itba.paw.interfaces.service.LocationService;
import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.exceptions.DuplicateEntityException;
import ar.edu.itba.paw.model.exceptions.EntityDependencyException;
import ar.edu.itba.paw.model.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationDao locationDao;

    @Autowired
    private ClinicService clinicService;

    @Transactional
    @Override
    public Location createLocation(String name) throws DuplicateEntityException {
        Optional<Location> exists = getLocationByName(name);
        if (exists.isPresent()) throw new DuplicateEntityException("location");
        return locationDao.createLocation(name);
    }

    @Override
    public List<Location> getLocations() { return locationDao.getLocations(); }

    @Override
    public List<Location> getPaginatedObjects(int page) {
        if(page < 0) {
            return Collections.emptyList();
        }
        return locationDao.getPaginatedObjects(page);
    }

    @Override
    public int maxAvailablePage() {
        return locationDao.maxAvailablePage();
    }

    @Override
    public Optional<Location> getLocationByName(String locationName) {
        return locationDao.getLocationByName(locationName);
    }

    @Transactional
    @Override
    public void deleteLocation(Location location) throws EntityDependencyException {
        List<Clinic> clinicsInLocation = clinicService.getClinicsByLocation(location);
        if (!clinicsInLocation.isEmpty()) throw new EntityDependencyException("clinics");
        locationDao.deleteLocation(location);
    }
}
