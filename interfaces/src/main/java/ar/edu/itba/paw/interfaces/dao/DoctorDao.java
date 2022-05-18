package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.*;

import java.util.List;
import java.util.Optional;

public interface DoctorDao extends PaginationDao<Doctor> {
    Doctor createDoctor(Specialty specialty, String license, String phoneNumber, User user);

    List<Doctor> getDoctors();

    List<Doctor> getDoctorBySpecialty(Specialty specialty);

    Optional<Doctor> getDoctorByLicense(String license);

    Optional<Doctor> getDoctorByEmail(String email);

    void updateDoctor(Doctor doctor, String license, Specialty specialty, String phoneNumber);

    List<Doctor> getPaginatedDoctorsInList(List<String> licenses, int page);
}
