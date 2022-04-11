package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.model.Prepaid;
import ar.edu.itba.paw.model.exceptions.DuplicateEntityException;
import ar.edu.itba.paw.model.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public interface PrepaidService extends PaginationService<Prepaid> {
    List<Prepaid> getPrepaid();

    Optional<Prepaid> getPrepaidByName(String PrepaidName);

    Prepaid createPrepaid(String name) throws DuplicateEntityException;

    void deletePrepaid(Prepaid prepaid);
}
