package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Specialty;

import java.util.List;

public interface SpecialtyDao extends PaginationDao<Specialty> {
    Specialty createSpecialty(String name);

    Specialty getSpecialtyByName(String SpecialtyName);

    List<Specialty> getSpecialties();

    long deleteSpecialty(String name);
}
