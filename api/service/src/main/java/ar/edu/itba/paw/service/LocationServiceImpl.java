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

@Component
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationDao locationDao;

    @Autowired
    private ClinicService clinicService;

    @Transactional
    @Override
    public Location createLocation(String name) throws DuplicateEntityException {
        Location location = getLocationByName(name);
        if (location != null) throw new DuplicateEntityException("location-exists");
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
    public Location getLocationByName(String locationName) {
        return locationDao.getLocationByName(locationName);
    }

    @Transactional
    @Override
    public void updateLocation(String oldName, String name) {
        locationDao.updateLocation(oldName, name);
    }

    @Transactional
    @Override
    public long deleteLocation(String name) throws EntityNotFoundException, EntityDependencyException {
        Location location = getLocationByName(name);
        if (location == null) throw new EntityNotFoundException("location");
        List<Clinic> clinicsInLocation = clinicService.getClinicsByLocation(location);
        if (!clinicsInLocation.isEmpty()) throw new EntityDependencyException("clinics");
        return locationDao.deleteLocation(name);
    }
}
