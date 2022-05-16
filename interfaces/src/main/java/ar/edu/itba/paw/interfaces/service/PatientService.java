package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.model.Doctor;
import ar.edu.itba.paw.model.Patient;
import ar.edu.itba.paw.model.Prepaid;
import ar.edu.itba.paw.model.exceptions.*;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    Patient create(String id, String prepaid, String prepaidNumber, String firstName, String lastName,
                   String password, String email) throws DuplicateEntityException, EntityNotFoundException, BadRequestException;

    Optional<Patient> getPatientByEmail(String email);

    List<Patient> getPatientsById(String id);

    List<Patient> getPatientsByPrepaid(Prepaid prepaid);

    void updatePatientProfile(Patient patient, String email, String newPassword, String firstName, String lastName,
                              String prepaid, String prepaidNumber) throws NoPrepaidNumberException;

    void updatePatient(Patient patient, String email, String prepaid, String prepaidNumber)
            throws NoPrepaidNumberException;

    void deletePatient(Patient patient);

    void addFavorite(Patient patient, Doctor doctor) throws FavouriteExistsException;

    void deleteFavorite(Patient patient, Doctor doctor);
}
