package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Prepaid;

import java.util.List;

public interface PrepaidDao extends PaginationDao<Prepaid> {

    Prepaid createPrepaid(String name);

    Prepaid getPrepaidByName(String PrepaidName);

    List<Prepaid> getPrepaids();

    void updatePrepaid(String oldName, String name);

    long deletePrepaid(String name);

}
