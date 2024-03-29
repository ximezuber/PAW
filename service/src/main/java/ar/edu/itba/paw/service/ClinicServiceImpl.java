package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.ClinicDao;
import ar.edu.itba.paw.interfaces.service.ClinicService;
import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.exceptions.DuplicateEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class ClinicServiceImpl implements ClinicService {

    @Autowired
    private ClinicDao clinicDao;

    @Transactional
    @Override
    public Clinic createClinic(String name, String address, Location location)
            throws DuplicateEntityException {
        if (clinicExists(name, address, location.getLocationName()))
            throw new DuplicateEntityException("clinic-exists");
        return clinicDao.createClinic(name, address, location);
    }

    @Override
    public List<Clinic> getClinics() {
        return clinicDao.getClinics();
    }

    @Override
    public Optional<Clinic> getClinicById(int id) {
        return clinicDao.getClinicById(id);
    }

    @Override
    public List<Clinic> getClinicsByLocation(Location location) {
        return clinicDao.getClinicsByLocation(location);
    }

    @Override
    public Optional<Clinic> getClinicByName(String name) {
        return clinicDao.getClinicByName(name);
    }

    @Override
    public boolean clinicExists(String name, String address, String location) {
        Optional<Clinic> clinic = clinicDao.getClinicByName(name);
        return clinic.filter(value -> value.getAddress().equals(address)
                        && value.getLocation().getLocationName().equals(location))
                .isPresent();
    }

    @Transactional
    @Override
    public void updateClinic(Clinic clinic, String name, String address, Location location) {
        if((name == null || name.equals("")) && (address == null || address.equals("")) && location == null)
            return;
        if(name == null || name.equals("")) {
            name = clinic.getName();
        }
        if(address == null || address.equals("")) {
            address = clinic.getAddress();
        }
        if(location == null) {
            location = clinic.getLocation();
        }
        clinicDao.updateClinic(clinic, name, address, location);
    }


    @Transactional
    @Override
    public void deleteClinic(Clinic clinic) {
        clinicDao.deleteClinic(clinic);
    }

    @Override
    public List<Clinic> getPaginatedObjects(int page) {
        if(page < 0) {
            return Collections.emptyList();
        }
        return clinicDao.getPaginatedObjects(page);
    }

    @Override
    public int maxAvailablePage() {
        return clinicDao.maxAvailablePage();
    }
}
