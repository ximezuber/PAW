package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.model.Specialty;
import ar.edu.itba.paw.model.exceptions.DuplicateEntityException;
import ar.edu.itba.paw.model.exceptions.EntityDependencyException;

import java.util.List;
import java.util.Optional;

public interface SpecialtyService extends PaginationService<Specialty> {

    Specialty createSpecialty(String name) throws DuplicateEntityException;

    List<Specialty> getSpecialties();

    Optional<Specialty> getSpecialtyByName(String SpecialtyName);

    void deleteSpecialty(Specialty specialty) throws EntityDependencyException;
}
