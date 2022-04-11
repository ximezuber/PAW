package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Specialty;

import java.util.List;
import java.util.Optional;

public interface SpecialtyDao extends PaginationDao<Specialty> {
    Specialty createSpecialty(String name);

    Optional<Specialty> getSpecialtyByName(String SpecialtyName);

    List<Specialty> getSpecialties();

    void deleteSpecialty(Specialty specialty);
}
