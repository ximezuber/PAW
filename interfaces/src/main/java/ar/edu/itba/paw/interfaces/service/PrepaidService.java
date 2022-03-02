package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.model.Prepaid;
import ar.edu.itba.paw.model.exceptions.DuplicateEntityException;
import ar.edu.itba.paw.model.exceptions.EntityNotFoundException;

import java.util.List;

public interface PrepaidService extends PaginationService<Prepaid> {
    List<Prepaid> getPrepaids();

    Prepaid getPrepaidByName(String PrepaidName);

    Prepaid createPrepaid(String name) throws DuplicateEntityException;

    void updatePrepaid(String oldName, String name);

    long deletePrepaid(String name) throws EntityNotFoundException;
}
