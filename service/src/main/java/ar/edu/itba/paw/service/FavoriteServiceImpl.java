package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.FavoriteDao;
import ar.edu.itba.paw.interfaces.service.FavoriteService;
import ar.edu.itba.paw.model.Doctor;
import ar.edu.itba.paw.model.Favorite;
import ar.edu.itba.paw.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Component
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteDao favoriteDao;

    @Transactional
    @Override
    public Favorite create(Doctor doctor, Patient patient) {
        return favoriteDao.create(doctor, patient);
    }

    @Override
    public List<Favorite> getPatientsFavorite(Patient patient) {
        return favoriteDao.getPatientsFavorite(patient);
    }

    @Override
    public boolean isFavorite(Doctor doctor, Patient patient) {
        return favoriteDao.isFavorite(doctor, patient);
    }

    @Transactional
    @Override
    public void deleteFavorite(Doctor doctor, Patient patient) {
        favoriteDao.deleteFavorite(doctor, patient);
    }

    @Override
    public List getPaginatedObjects(int page, Patient patient) {
        if(page < 0) {
            return Collections.emptyList();
        }
        return favoriteDao.getPaginatedObjects(page, patient);
    }

    @Override
    public int maxAvailablePage(Patient patient) {
        return favoriteDao.maxAvailablePage(patient);
    }
}
