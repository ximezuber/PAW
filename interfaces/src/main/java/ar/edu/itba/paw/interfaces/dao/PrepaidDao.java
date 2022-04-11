package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Prepaid;

import java.util.List;
import java.util.Optional;

public interface PrepaidDao extends PaginationDao<Prepaid> {

    Prepaid createPrepaid(String name);

    Optional<Prepaid> getPrepaidByName(String PrepaidName);

    List<Prepaid> getPrepaid();

    void deletePrepaid(Prepaid prepaid);

}
