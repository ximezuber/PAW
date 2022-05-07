package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.model.Doctor;
import ar.edu.itba.paw.model.Favorite;
import ar.edu.itba.paw.model.Patient;

import java.util.List;
import java.util.Optional;

public interface FavoriteService {

    Favorite create(Doctor doctor, Patient patient);

    List<Favorite> getPatientsFavorite(Patient patient);

    Optional<Favorite> getFavorite(Doctor doctor, Patient patient);

    void deleteFavorite(Favorite favorite);

    List<Favorite> getPaginatedObjects(int page, Patient patient);

    int maxAvailablePage(Patient patient);
}
