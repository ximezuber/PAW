package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.exceptions.DuplicateEntityException;
import ar.edu.itba.paw.model.exceptions.EntityNotFoundException;

import java.util.List;

public interface ClinicService extends PaginationService<Clinic> {
    Clinic createClinic(String name, String address, Location location) throws DuplicateEntityException;

    List<Clinic> getClinics();

    Clinic getClinicById(int id);

    List<Clinic> getClinicsByLocation(Location location);

    boolean clinicExists(String name,String address,String location);

    void updateClinic(int id, String name, String address, String location);

    long deleteClinic(int id) throws EntityNotFoundException;
}
