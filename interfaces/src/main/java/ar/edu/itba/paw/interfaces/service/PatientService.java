package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.model.Doctor;
import ar.edu.itba.paw.model.Patient;
import ar.edu.itba.paw.model.exceptions.DuplicateEntityException;
import ar.edu.itba.paw.model.exceptions.EntityNotFoundException;
import ar.edu.itba.paw.model.exceptions.FavouriteExistsException;

import java.util.List;

public interface PatientService {
    Patient create(String id, String prepaid, String prepaidNumber, String firstName, String lastName, String password, String email) throws DuplicateEntityException;

    Patient getPatientByEmail(String email);

    List<Doctor> getPatientFavoriteDoctors(Patient patient);

    void updatePatientProfile(String email, String newPassword, String firstName, String lastName, String prepaid, String prepaidNumber);

    void setAppointments(Patient patient);

    void updatePatient(String email, String prepaid, String prepaidNumber);

    List<Patient> getPatientsByPrepaid(String prepaid);

    void deletePatient(String email);

    List<Patient> getPatientsById(String id);

    void addFavorite(String patientEmail, String license) throws FavouriteExistsException, EntityNotFoundException;

    void deleteFavorite(String patientEmail, String license);
}
