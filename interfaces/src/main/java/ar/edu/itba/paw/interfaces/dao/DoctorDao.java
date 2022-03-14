package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.*;

import java.util.List;
import java.util.Map;

public interface DoctorDao extends PaginationDao<Doctor> {
    Doctor createDoctor(Specialty specialty, String license, String phoneNumber, User user);

    List<Doctor> getDoctors();

    List<Doctor> getDoctorBySpecialty(Specialty specialty);

    Doctor getDoctorByLicense(String license);

    boolean isDoctor(String email);

    Doctor getDoctorByEmail(String email);

    void updateDoctor(String license, Map<String, String> args);

    List<Doctor> getPaginatedDoctorsInList(List<String> licenses, int page);

    int maxAvailableDoctorsInListPage(List<String> licenses);
}
