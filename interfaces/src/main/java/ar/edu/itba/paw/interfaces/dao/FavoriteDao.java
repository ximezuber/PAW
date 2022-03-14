package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Doctor;
import ar.edu.itba.paw.model.Favorite;
import ar.edu.itba.paw.model.Patient;

import java.util.List;

public interface FavoriteDao {

    Favorite create(Doctor doctor, Patient patient);

    List<Favorite> getPatientsFavorite(Patient patient);

    boolean isFavorite(Doctor doctor, Patient patient);


    void deleteFavorite(Doctor doctor, Patient patient);

    List<Favorite> getPaginatedObjects(int page, Patient patient);

    int maxAvailablePage(Patient patient);
}
