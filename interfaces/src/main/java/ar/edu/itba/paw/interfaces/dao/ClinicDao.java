package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.Location;

import java.util.List;
import java.util.Optional;

public interface ClinicDao extends PaginationDao<Clinic> {
    Clinic createClinic(String name, String address, Location location);

    List<Clinic> getClinics();

    Optional<Clinic> getClinicById(int id);

    List<Clinic> getClinicsByLocation(Location location);

    Optional<Clinic> getClinicByName(String name);

    void updateClinic(Clinic clinic, String name, String address, Location location);

    void deleteClinic(Clinic clinic);
}
