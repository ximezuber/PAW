package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.DoctorDao;
import ar.edu.itba.paw.interfaces.service.DoctorClinicService;
import ar.edu.itba.paw.interfaces.service.DoctorService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.DuplicateEntityException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jmx.export.annotation.ManagedOperation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class DoctorServiceImplTest {

    private static final Specialty specialty = new Specialty("specialty");

    private static final String license = "4";

    private static final Location location = new Location("location");

    private static final Clinic clinic = new Clinic(1,"clinic", "address", location);

    private static final int consultPrice = 1;

    private static final String phone = "1232214321";

    private static final int day = Calendar.MONDAY;

    private static final int hour = 10;

    private static final String email ="patient@mail.com";
    private static final String firstName = "firstName";
    private static final String lastName = "lastName";
    private static final String password = "password";

    private static final User user = new User(firstName, lastName, password, email);

    private static final Doctor doctor = new Doctor(specialty, license, phone, user);

    private static final DoctorClinic doctorClinic = new DoctorClinic(doctor, clinic, consultPrice);


    @InjectMocks
    private DoctorServiceImpl doctorService = new DoctorServiceImpl();

    @Mock
    private DoctorDao mockDao;

    @Mock
    private DoctorClinicService doctorClinicService;

    @Mock
    private UserService userService;

    @Test
    public void testCreate() throws DuplicateEntityException {
        //Set Up
        Mockito.when(mockDao.getDoctorByLicense(Mockito.eq(license)))
                .thenReturn(null);
        Mockito.when(userService.findUserByEmail(Mockito.eq(email)))
                .thenReturn(null);
        Mockito.when(userService.createUser(Mockito.eq(firstName), Mockito.eq(lastName), Mockito.eq(password), Mockito.eq(email)))
                        .thenReturn(user);
        Mockito.when(mockDao.createDoctor(Mockito.eq(specialty), Mockito.eq(license), Mockito.eq(phone), Mockito.eq(user)))
                .thenReturn(new Doctor(specialty, license, phone, user));

        //Execute
        Doctor doctor = doctorService.createDoctor(specialty, license, phone, firstName,
                lastName, password, email);

        //Assert
        Assert.assertNotNull(doctor);
        Assert.assertEquals(license, doctor.getLicense());
        Assert.assertEquals(user.getLastName(), doctor.getLastName());
        Assert.assertEquals(user.getFirstName(), doctor.getFirstName());
        Assert.assertEquals(phone, doctor.getPhoneNumber());
        Assert.assertEquals(user.getEmail(), doctor.getEmail());
    }

    @Test(expected = DuplicateEntityException.class)
    public void testCreateDocExists() throws DuplicateEntityException {
        //Set Up
        Mockito.when(mockDao.getDoctorByLicense(Mockito.eq(license)))
                .thenReturn(doctor);
        //Execute
        doctorService.createDoctor(specialty, license, phone, firstName,
                lastName, password, email);
    }

    @Test(expected = DuplicateEntityException.class)
    public void testCreateUserExists() throws DuplicateEntityException {
        //Set Up
        Mockito.when(mockDao.getDoctorByLicense(Mockito.eq(license)))
                .thenReturn(null);
        Mockito.when(userService.findUserByEmail(Mockito.eq(email)))
                .thenReturn(user);
        //Execute
        doctorService.createDoctor(specialty, license, phone, firstName,
                lastName, password, email);
    }

        @Test
    public void testGetDoctorsWithAvailability(){
        //Set Up
        List<Schedule> s = new ArrayList<>();
        s.add(new Schedule(day, hour, doctorClinic));
        List<Doctor> docs = new ArrayList<>();
        docs.add(doctor);
        Mockito.when(mockDao.getDoctors())
                .thenReturn(docs);
        doctorClinic.setSchedule(s);
        List<DoctorClinic> doctorClinics = new ArrayList<>();
        doctorClinics.add(doctorClinic);
        Mockito.when(doctorClinicService.getDoctorClinicsForDoctor(doctor))
                .thenReturn(doctorClinics);

        //Execute
        List<Doctor> doctors = doctorService.getDoctorsWithAvailability();

        //Assert
        Assert.assertNotNull(doctors);
        Assert.assertFalse(doctors.isEmpty());
        Assert.assertEquals(doctor.getLicense(), doctors.get(0).getLicense());

    }
}
