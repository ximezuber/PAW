package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.DoctorDao;
import ar.edu.itba.paw.interfaces.service.DoctorClinicService;
import ar.edu.itba.paw.interfaces.service.DoctorService;
import ar.edu.itba.paw.interfaces.service.ImageService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.DuplicateEntityException;
import ar.edu.itba.paw.model.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;

@Component
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorDao doctorDao;

    @Autowired
    private DoctorClinicService doctorClinicService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @Transactional
    @Override
    public Doctor createDoctor(Specialty specialty, String license, String phoneNumber,
                               String firstName, String lastName, String password, String email)
            throws DuplicateEntityException {
        Doctor isDoctor = getDoctorByLicense(license);
        if (isDoctor != null) throw new DuplicateEntityException("license-in-use");
        if (userService.findUserByEmail(email) != null) throw new DuplicateEntityException("email-in-use");
        User user = userService.createUser(firstName, lastName, password, email);
        return doctorDao.createDoctor(specialty, license, phoneNumber, user);
    }

    @Override
    public List<Doctor> getDoctors() {
        return doctorDao.getDoctors();
    }

    @Override
    public List<Doctor> getDoctorBySpecialty(Specialty specialty) {
        return doctorDao.getDoctorBySpecialty(specialty);
    }

    @Override
    public Doctor getDoctorByLicense(String license) {
        return doctorDao.getDoctorByLicense(license);
    }

    @Override
    public Doctor getDoctorByEmail(String email) {
        return doctorDao.getDoctorByEmail(email);
    }

    @Override
    public boolean isDoctor(String email) {
        return doctorDao.isDoctor(email);
    }

    @Transactional
    @Override
    public long deleteDoctor(String license) throws EntityNotFoundException {
        Doctor doc = getDoctorByLicense(license);
        if (doc == null) throw new EntityNotFoundException("doctor");
        return userService.deleteUser(doc.getEmail());
    }

    @Transactional
    @Override
    public void updateDoctor(String license, String phoneNumber, String specialty) {
        Map<String, String> args = new HashMap<>();
        args.put("phoneNumber", phoneNumber);
        if (specialty.equals("")) {
            specialty = null;
        }
        args.put("specialty", specialty);
        doctorDao.updateDoctor(license, args);
    }

    @Transactional
    @Override
    public void updateDoctorProfile(
            String email, String newPassword, String firstName, String lastName, // updates user fields
            String phoneNumber, String specialty) { // updates image field

        userService.updateUser(email, newPassword, firstName, lastName);
        Doctor doctor = getDoctorByEmail(email);
        updateDoctor(doctor.getLicense(), phoneNumber, specialty);
    }

    @Override
    public List<String> getFilteredLicenses(Location location, Specialty specialty,
                                                     String firstName, String lastName,
                                                     Prepaid prepaid, int consultPrice, boolean includeUnavailable) {
        // Todo: see a better way of doing this
        List<String> licenses = new ArrayList<>();
        List<DoctorClinic> doctorClinics = doctorClinicService.getFilteredDoctorClinics(location, specialty,
                firstName, lastName, prepaid, consultPrice);
        if (!includeUnavailable) {
            for (DoctorClinic dc : doctorClinics) {
                if ((!dc.getSchedule().isEmpty()) && !(licenses.contains(dc.getDoctor().getLicense()))) {
                    licenses.add(dc.getDoctor().getLicense());
                }
            }
        } else {
            for (DoctorClinic dc : doctorClinics) {
                licenses.add(dc.getDoctor().getLicense());
            }
        }

        return licenses;
    }

    @Override
    public List<Doctor> getPaginatedDoctors(List<String> licenses, int page) {
        if (page < 0 || licenses.isEmpty()) {
            return Collections.emptyList();
        }

        //not inline for debugging purposes
        List<Doctor> doctors = doctorDao.getPaginatedDoctorsInList(licenses, page);
        return doctors;
    }

    @Override
    public int getMaxAvailableDoctorsPage(List<String> licenses) {
        return licenses.isEmpty() ? 0 : doctorDao.maxAvailableDoctorsInListPage(licenses);
    }

    @Override
    public List<Doctor> getPaginatedObjects(int page) {
        if (page < 0) {
            return Collections.emptyList();
        }
        return doctorDao.getPaginatedObjects(page);
    }

    @Override
    public int maxAvailablePage() {
        return doctorDao.maxAvailablePage();
    }
}
