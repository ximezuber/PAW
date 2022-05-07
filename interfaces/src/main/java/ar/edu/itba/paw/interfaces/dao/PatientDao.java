package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Patient;
import ar.edu.itba.paw.model.Prepaid;
import ar.edu.itba.paw.model.User;

import java.util.List;
import java.util.Optional;

public interface PatientDao {
    Patient create(String id, Prepaid prepaid, String prepaidNumber, User user);

    Optional<Patient> getPatientByEmail(String email);

    void updatePatient(Patient patient, String id, Prepaid prepaid, String prepaidNumber);

    List<Patient> getPatientsByPrepaid(Prepaid prepaid);

    List<Patient> getPatientsById(String id);

}
