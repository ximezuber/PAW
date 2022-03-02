package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.model.Specialty;
import ar.edu.itba.paw.model.exceptions.DuplicateEntityException;
import ar.edu.itba.paw.model.exceptions.EntityDependencyException;
import ar.edu.itba.paw.model.exceptions.EntityNotFoundException;

import java.util.List;

public interface SpecialtyService extends PaginationService<Specialty> {

    Specialty createSpecialty(String name) throws DuplicateEntityException;

    List<Specialty> getSpecialties();

    Specialty getSpecialtyByName(String SpecialtyName);

    void updateSpecialty(String oldName, String name);

    long deleteSpecialty(String name) throws EntityNotFoundException, EntityDependencyException;
}
