package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.ClinicDao;
import ar.edu.itba.paw.interfaces.service.ClinicService;
import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.exceptions.DuplicateEntityException;
import ar.edu.itba.paw.model.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Component
public class ClinicServiceImpl implements ClinicService {

    @Autowired
    private ClinicDao clinicDao;

    @Transactional
    @Override
    public Clinic createClinic(String name, String address, Location location) throws DuplicateEntityException {
        if (clinicExists(name, address, location.getLocationName())) throw new DuplicateEntityException("clinic-exists");
        return clinicDao.createClinic(name, address, location);
    }

    @Override
    public List<Clinic> getClinics() {
        return clinicDao.getClinics();
    }

    @Override
    public Clinic getClinicById(int id) {
        return clinicDao.getClinicById(id);
    }

    @Override
    public List<Clinic> getClinicsByLocation(Location location) {
        return clinicDao.getClinicsByLocation(location);
    }

    @Override
    public boolean clinicExists(String name, String address, String location) {
        return clinicDao.clinicExists(name, address, new Location(location));
    }

    @Transactional
    @Override
    public void updateClinic(int id, String name, String address, String location) {
        if(name.equals("") && address.equals("") && location.equals(""))
            return;
        Clinic clinic = getClinicById(id);
        if(name.equals("")) {
            name = clinic.getName();
        }
        if(address.equals("")) {
            address = clinic.getAddress();
        }
        if(location.equals("")) {
            location = clinic.getLocation().getLocationName();
        }
        clinicDao.updateClinic(id, name, address, location);
    }


    @Transactional
    @Override
    public long deleteClinic(int id) throws EntityNotFoundException {
        Clinic clinic = getClinicById(id);
        if(clinic == null) throw new EntityNotFoundException("clinic");
        return clinicDao.deleteClinic(id);
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
