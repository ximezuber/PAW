package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.DoctorDao;
import ar.edu.itba.paw.interfaces.service.DoctorService;
import ar.edu.itba.paw.interfaces.service.SpecialtyService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.DuplicateEntityException;
import ar.edu.itba.paw.model.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

@Component
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorDao doctorDao;

    @Autowired
    private UserService userService;

    @Autowired
    private SpecialtyService specialtyService;

    @Transactional
    @Override
    public Doctor createDoctor(String specialtyName, String license, String phoneNumber,
                               String firstName, String lastName, String password, String email)
            throws DuplicateEntityException, EntityNotFoundException {
        Specialty specialty = specialtyService.getSpecialtyByName(specialtyName)
                .orElseThrow(() -> new EntityNotFoundException("specialty"));

        if (getDoctorByLicense(license).isPresent()) throw new DuplicateEntityException("license-in-use");
        if (userService.findUserByEmail(email).isPresent()) throw new DuplicateEntityException("email-in-use");
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
    public Optional<Doctor> getDoctorByLicense(String license) {
        return doctorDao.getDoctorByLicense(license);
    }

    @Override
    public Optional<Doctor> getDoctorByEmail(String email) {
        return doctorDao.getDoctorByEmail(email);
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

    @Transactional
    @Override
    public void deleteDoctor(String license) throws EntityNotFoundException {
        Doctor doctor = getDoctorByLicense(license)
                .orElseThrow(() -> new EntityNotFoundException("doctor"));
        userService.deleteUser(doctor.getUser());
    }

    @Transactional
    @Override
    public void updateDoctorProfile (Doctor doctor,
            String email, String newPassword, String firstName, String lastName,
            String phoneNumber, String newSpecialty) {

        userService.updateUser(doctor.getUser(), email, newPassword, firstName, lastName);
        Specialty specialty;
        if (phoneNumber == null || phoneNumber.equals(""))
            phoneNumber = doctor.getPhoneNumber();
        if (newSpecialty == null || newSpecialty.equals("")) {
            specialty = doctor.getSpecialty();
        } else {
            specialty = specialtyService.getSpecialtyByName(newSpecialty).orElse(doctor.getSpecialty());
        }
        doctorDao.updateDoctor(doctor, doctor.getLicense(), specialty, phoneNumber);
    }
}
