package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.DuplicateEntityException;
import ar.edu.itba.paw.model.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public interface DoctorService extends PaginationService<Doctor> {
    Doctor createDoctor(String specialty, String license, String phoneNumber, String firstName,
                        String lastName, String password, String email) throws DuplicateEntityException, EntityNotFoundException;

    List<Doctor> getDoctors();

    List<Doctor> getDoctorBySpecialty(Specialty specialty);

    Optional<Doctor> getDoctorByLicense(String license);

    Optional<Doctor> getDoctorByEmail(String email);

//    List<Doctor> getPaginatedDoctors(List<String> licenses, int page);

    int getMaxAvailableDoctorsPage(List<String> licenses);

    void deleteDoctor(String license) throws EntityNotFoundException;

    void updateDoctorProfile (Doctor doctor,
            String email, String newPassword, String firstName, String lastName,
            String phoneNumber, String specialty);

}
