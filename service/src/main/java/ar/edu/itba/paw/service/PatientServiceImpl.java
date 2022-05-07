package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.PatientDao;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.DuplicateEntityException;
import ar.edu.itba.paw.model.exceptions.FavouriteExistsException;
import ar.edu.itba.paw.model.exceptions.NoPrepaidNumberException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Component
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientDao patientDao;

    @Autowired
    private PrepaidService prepaidService;

    @Autowired
    private UserService userService;

    @Autowired
    private FavoriteService favoriteService;

    private static final String NoPrepaid = "";

    @Transactional
    @Override
    public Patient create(String id, String prepaidName, String prepaidNumber, String firstName,
                          String lastName, String password, String email) throws DuplicateEntityException,
            EntityNotFoundException {
        if (userService.findUserByEmail(email).isPresent()) throw new DuplicateEntityException("user-exists");
        Prepaid prepaid;
        if(prepaidName == null || prepaidName.equals(NoPrepaid)) {
            prepaid = null;
            prepaidNumber = null;
        } else {
            prepaid = prepaidService.getPrepaidByName(prepaidName)
                    .orElseThrow(() -> new EntityNotFoundException("prepaid"));

        }
        User user = userService.createUser(firstName, lastName, password, email);
        return patientDao.create(id, prepaid, prepaidNumber, user);
    }

    @Override
    public Optional<Patient> getPatientByEmail(String email) {
        return patientDao.getPatientByEmail(email);
    }

    @Override
    public List<Patient> getPatientsById(String id){
        return patientDao.getPatientsById(id);
    }

    @Override
    public List<Patient> getPatientsByPrepaid(Prepaid prepaid) {
        return patientDao.getPatientsByPrepaid(prepaid);
    }

    @Transactional
    @Override
    public void updatePatientProfile(Patient patient, String email, String newPassword, String firstName,
                                     String lastName, String prepaid, String prepaidNumber) throws NoPrepaidNumberException {
        userService.updateUser(patient.getUser(), email, newPassword, firstName, lastName);
        updatePatient(patient, email, prepaid, prepaidNumber);
    }

    @Transactional
    @Override
    public void updatePatient(Patient patient, String email, String prepaidName, String prepaidNumber)
            throws NoPrepaidNumberException {
        Prepaid prepaid;
        if(prepaidName == null || prepaidName.equals("")){
            prepaid = null;
            prepaidNumber = null;
        } else {
            prepaid = prepaidService.getPrepaidByName(prepaidName)
                    .orElseThrow(() -> new EntityNotFoundException("prepaid"));
            if (prepaidNumber == null || prepaidNumber.equals("")) throw new NoPrepaidNumberException();
        }

        patientDao.updatePatient(patient, email, prepaid, prepaidNumber);
    }

    @Override
    @Transactional
    public void deletePatient(Patient patient) {
        userService.deleteUser(patient.getUser());
    }


    @Override
    public void addFavorite(Patient patient, Doctor doctor) throws FavouriteExistsException,
            EntityNotFoundException {
        if (!favoriteService.getFavorite(doctor, patient).isPresent()){
            favoriteService.create(doctor,patient);
        } else {
            throw new FavouriteExistsException();
        }
    }

    @Override
    public void deleteFavorite(Patient patient, Doctor doctor) throws EntityNotFoundException {
        Favorite favorite = favoriteService.getFavorite(doctor, patient)
                .orElseThrow(() -> new EntityNotFoundException("favorite"));
        favoriteService.deleteFavorite(favorite);

    }
}
