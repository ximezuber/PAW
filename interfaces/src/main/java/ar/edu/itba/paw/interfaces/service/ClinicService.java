package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.exceptions.DuplicateEntityException;
import ar.edu.itba.paw.model.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public interface ClinicService extends PaginationService<Clinic> {
    Clinic createClinic(String name, String address, Location location) throws DuplicateEntityException;

    List<Clinic> getClinics();

    Optional<Clinic> getClinicById(int id);

    List<Clinic> getClinicsByLocation(Location location);

    Optional<Clinic> getClinicByName(String name);

    boolean clinicExists(String name, String address, String location);

    void updateClinic(Clinic clinic, String name, String address, Location location);

    void deleteClinic(Clinic clinic) throws EntityNotFoundException;
}
