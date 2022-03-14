package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.DuplicateEntityException;
import ar.edu.itba.paw.model.exceptions.EntityNotFoundException;

import java.util.List;

public interface DoctorService extends PaginationService<Doctor> {
    Doctor createDoctor(Specialty specialty, String license, String phoneNumber, String firstName,
                        String lastName, String password, String email) throws DuplicateEntityException;

    List<Doctor> getDoctors();

    List<Doctor> getDoctorBySpecialty(Specialty specialty);

    Doctor getDoctorByLicense(String license);

    Doctor getDoctorByEmail(String email);

    List<Doctor> getPaginatedDoctors(List<String> licenses, int page);

    int getMaxAvailableDoctorsPage(List<String> licenses);

    boolean isDoctor(String email);

    long deleteDoctor(String license) throws EntityNotFoundException;

    void updateDoctorProfile(
            String email, String newPassword, String firstName, String lastName,
            String phoneNumber, String specialty);

}
