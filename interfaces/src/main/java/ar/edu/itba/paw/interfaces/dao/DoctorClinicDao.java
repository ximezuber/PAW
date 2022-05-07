package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.*;

import java.util.List;
import java.util.Optional;

public interface DoctorClinicDao {
        DoctorClinic createDoctorClinic(Doctor doctor, Clinic clinic, int consultPrice);

        void deleteDoctorClinic(DoctorClinic doctorClinic);

        List<DoctorClinic> getDoctorsSubscribedClinics(Doctor doctor);

        Optional<DoctorClinic> getDoctorClinic(Doctor doctor, Clinic clinic);

        List<Doctor> getFilteredDoctorInClinics(Location location, Specialty specialty,
                                                String firstName, String lastName, Prepaid prepaid,
                                                int consultPrice);

        List<Doctor> getFilteredDoctorInClinicsPaginated(Location location, Specialty specialty,
                                                         String firstName, String lastName, Prepaid prepaid,
                                                         int consultPrice, int page);



        List<DoctorClinic> getDoctorClinicPaginatedByList(Doctor doctor, int page);

        int maxAvailableFilteredDoctorClinicPage(Location location, Specialty specialty,
                                                 String firstName, String lastName, Prepaid prepaid,
                                                 int consultPrice);

        int maxPageAvailable(Doctor doctor);

        void editPrice(DoctorClinic dc, int price);
}
