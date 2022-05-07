package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.DuplicateEntityException;

import java.util.List;
import java.util.Optional;

public interface DoctorClinicService {
    DoctorClinic createDoctorClinic(Doctor doctor, Clinic clinic, int consultPrice)
            throws DuplicateEntityException;

    void deleteDoctorClinic(DoctorClinic doctorClinic);

    List<DoctorClinic> getDoctorsSubscribedClinics(Doctor doctor);

    Optional<DoctorClinic> getDoctorClinic(Doctor doctor, Clinic clinic);

    Optional<DoctorClinic> getDoctorClinicWithSchedule(Doctor doctor, Clinic clinic);

    List<Doctor> getPaginatedFilteredDoctorClinics(Location location, Specialty specialty,
                                                         String firstName, String lastName, Prepaid prepaid,
                                                         int consultPrice, int page);

    List<DoctorClinic> getPaginatedDoctorsClinics(Doctor doctor, int page);

    void editPrice(DoctorClinic dc, int price);

    int maxAvailableFilteredDoctorClinicPage(Location location, Specialty specialty,
                                             String firstName, String lastName, Prepaid prepaid,
                                             int consultPrice);

    int maxAvailablePage(Doctor doctor);

}
