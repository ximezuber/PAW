package ar.edu.itba.paw.interfaces.dao;

import java.util.List;

public interface PaginationDao<T> {
    List<T> getPaginatedObjects(int page);

    int maxAvailablePage();
}
