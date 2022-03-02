package ar.edu.itba.paw.interfaces.service;

import java.util.List;

public interface PaginationService<T> {
    List<T> getPaginatedObjects(int page);

    int maxAvailablePage();
}
