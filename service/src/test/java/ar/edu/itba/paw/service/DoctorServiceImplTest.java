//package ar.edu.itba.paw.service;
//
//import ar.edu.itba.paw.interfaces.dao.DoctorDao;
//import ar.edu.itba.paw.interfaces.dao.UserDao;
//import ar.edu.itba.paw.interfaces.service.DoctorClinicService;
//import ar.edu.itba.paw.interfaces.service.SpecialtyService;
//import ar.edu.itba.paw.interfaces.service.UserService;
//import ar.edu.itba.paw.model.*;
//import ar.edu.itba.paw.model.exceptions.DuplicateEntityException;
//import ar.edu.itba.paw.model.exceptions.EntityNotFoundException;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.util.Calendar;
//import java.util.Optional;
//
//@RunWith(MockitoJUnitRunner.class)
//public class DoctorServiceImplTest {
//
//    private static final Specialty specialty = new Specialty("specialty");
//
//    private static final String license = "4";
//
//    private static final Location location = new Location("location");
//
//    private static final Clinic clinic = new Clinic(1,"clinic", "address", location);
//
//    private static final int consultPrice = 1;
//
//    private static final String phone = "1232214321";
//
//    private static final int day = Calendar.MONDAY;
//
//    private static final int hour = 10;
//
//    private static final String email ="patient@mail.com";
//    private static final String firstName = "firstName";
//    private static final String lastName = "lastName";
//    private static final String password = "password";
//
//    private static final User user = new User(firstName, lastName, password, email);
//
//    private static final Doctor doctor = new Doctor(specialty, license, phone, user);
//
//    private static final DoctorClinic doctorClinic = new DoctorClinic(doctor, clinic, consultPrice);
//
//
//    @InjectMocks
//    private DoctorServiceImpl doctorService = new DoctorServiceImpl();
//
//    @Mock
//    private DoctorDao mockDao;
//
//    @Mock
//    private UserDao userDao;
//
//    @Mock
//    private DoctorClinicService doctorClinicService;
//
//    @Mock
//    private UserService userService;
//
//    @Mock
//    private SpecialtyService specialtyService;
//
////    @Test
////    public void testCreate() throws DuplicateEntityException, EntityNotFoundException {
////        //Set Up
//////        Mockito.when(mockDao.getDoctorByLicense(Mockito.eq(license)))
//////                .thenReturn(Optional.empty());
//////        Mockito.when(userService.findUserByEmail(Mockito.eq(email)))
//////                .thenReturn(Optional.empty());
////        Mockito.when(specialtyService.getSpecialtyByName(Mockito.eq(specialty.getSpecialtyName())))
////                .thenReturn(Optional.of(specialty));
//////        Mockito.when(userService.createUser(Mockito.eq(firstName), Mockito.eq(lastName), Mockito.eq(password), Mockito.eq(email)))
//////                        .thenReturn(user);
////        Mockito.when(mockDao.createDoctor(Mockito.eq(specialty), Mockito.eq(license), Mockito.eq(phone), Mockito.eq(user)))
////                .thenReturn(new Doctor(specialty, license, phone, user));
////        Mockito.when(userDao.createUser(firstName, lastName, password, email))
////                .thenReturn(user);
////
////        //Execute
////        Doctor doctor = doctorService.create(specialty.getSpecialtyName(), license, phone, firstName,
////                lastName, password, email);
////
////        //Assert
////        Assert.assertNotNull(doctor);
////        Assert.assertEquals(license, doctor.getLicense());
////        Assert.assertEquals(user.getLastName(), doctor.getLastName());
////        Assert.assertEquals(user.getFirstName(), doctor.getFirstName());
////        Assert.assertEquals(phone, doctor.getPhoneNumber());
////        Assert.assertEquals(user.getEmail(), doctor.getEmail());
////    }
//
////    @Test(expected = DuplicateEntityException.class)
////    public void testCreateDocExists() throws DuplicateEntityException, EntityNotFoundException {
////        //Set Up
////        Mockito.when(mockDao.getDoctorByLicense(Mockito.eq(license)))
////                .thenReturn(Optional.of(doctor));
////        //Execute
////        doctorService.create(specialty.getSpecialtyName(), license, phone, firstName,
////                lastName, password, email);
////    }
//
////    @Test(expected = DuplicateEntityException.class)
////    public void testCreateUserExists() throws DuplicateEntityException, EntityNotFoundException {
////        //Set Up
////        Mockito.when(mockDao.getDoctorByLicense(Mockito.eq(license)))
////                .thenReturn(Optional.empty());
////        Mockito.when(userService.findUserByEmail(Mockito.eq(email)))
////                .thenReturn(Optional.of(user));
////        //Execute
////        doctorService.create(specialty.getSpecialtyName(), license, phone, firstName,
////                lastName, password, email);
////    }
//}
