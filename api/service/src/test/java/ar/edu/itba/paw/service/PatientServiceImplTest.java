package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.PatientDao;
import ar.edu.itba.paw.interfaces.service.AppointmentService;
import ar.edu.itba.paw.interfaces.service.DoctorService;
import ar.edu.itba.paw.interfaces.service.FavoriteService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.DuplicateEntityException;
import ar.edu.itba.paw.model.exceptions.FavouriteExistsException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class PatientServiceImplTest {

    private static final User user = new User("patFirstName", "patLastName", "password", "patient@mail.com");
    private static final User user2 = new User("docFirstName", "docLastName", "password", "doc@mail.com");

    private static final String id = "332233";

    private static final Prepaid prepaid = new Prepaid("prepaid");

    private static final String prepaidNumber = "123231";

    private static final String license = "license";
    private static final Doctor doctor = new Doctor(new Specialty("specialty"), license, "1234567", user2);

    @InjectMocks
    private PatientServiceImpl patientService = new PatientServiceImpl();

    @Mock
    private PatientDao mockDao;

    @Mock
    private UserService userService;

    @Mock
    private DoctorService doctorService;

    @Mock
    private FavoriteService favoriteService;

    @Test
    public void testCreate() throws DuplicateEntityException {
        //Set Up
        Mockito.when(mockDao.create(Mockito.eq(id),Mockito.eq(prepaid.getName()),
                Mockito.eq(prepaidNumber), Mockito.eq(user)))
                .thenReturn(new Patient(id, prepaid.getName(), prepaidNumber, user));
        Mockito.when(userService.createUser(Mockito.eq(user.getFirstName()), Mockito.eq(user.getLastName()),
                Mockito.eq(user.getPassword()), Mockito.eq(user.getEmail()))).thenReturn(user);
        //Execute
        Patient patient = patientService.create(id, prepaid.getName(), prepaidNumber, user.getFirstName(), user.getLastName(), user.getPassword(), user.getEmail());

        //Assert
        Assert.assertNotNull(patient);
        Assert.assertEquals(user.getEmail(), patient.getEmail());
        Assert.assertEquals(id, patient.getId());
        Assert.assertEquals(prepaid.getName(), patient.getPrepaid());
        Assert.assertEquals(prepaidNumber, patient.getPrepaidNumber());
        Assert.assertEquals(user.getLastName(), patient.getLastName());
        Assert.assertEquals(user.getFirstName(), patient.getFirstName());

    }

    @Test(expected = DuplicateEntityException.class)
    public void testCreateEmailExists() throws DuplicateEntityException {
        // Set up
        Mockito.when(userService.userExists(user.getEmail()))
                .thenReturn(true);

        // Execute
       patientService.create(id, prepaid.getName(), prepaidNumber, user.getFirstName(), user.getLastName(), user.getPassword(), user.getEmail());

    }

    @Test
    public void tessAddFavorites() throws FavouriteExistsException {
        // Set up
        Patient patient = new Patient(id, prepaid.getName(), prepaidNumber, user);
        Mockito.when(doctorService.getDoctorByLicense(license))
                .thenReturn(doctor);
        Mockito.when(mockDao.getPatientByEmail(user.getEmail()))
                .thenReturn(patient);
        Mockito.when(favoriteService.isFavorite(Mockito.eq(doctor), Mockito.eq(patient)))
                .thenReturn(false);


        // Execute
        patientService.addFavorite(user.getEmail(), license);
    }

    @Test(expected = EntityNotFoundException.class)
    public void tessAddFavoritesNoDoc() throws FavouriteExistsException, EntityNotFoundException {
        // Set up
        Patient patient = new Patient(id, prepaid.getName(), prepaidNumber, user);
        Mockito.when(doctorService.getDoctorByLicense(license))
                .thenReturn(null);

        // Execute
        patientService.addFavorite(user.getEmail(), license);
    }

    @Test(expected = EntityNotFoundException.class)
    public void tessAddFavoritesNoPatient() throws FavouriteExistsException, EntityNotFoundException {
        // Set up
        Patient patient = new Patient(id, prepaid.getName(), prepaidNumber, user);
        Mockito.when(doctorService.getDoctorByLicense(license))
                .thenReturn(doctor);
        Mockito.when(mockDao.getPatientByEmail(user.getEmail()))
                .thenReturn(null);

        // Execute
        patientService.addFavorite(user.getEmail(), license);
    }

    @Test(expected = FavouriteExistsException.class)
    public void tessAddFavoritesFavExists() throws FavouriteExistsException, EntityNotFoundException {
        // Set up
        Patient patient = new Patient(id, prepaid.getName(), prepaidNumber, user);
        Mockito.when(doctorService.getDoctorByLicense(license))
                .thenReturn(doctor);
        Mockito.when(mockDao.getPatientByEmail(user.getEmail()))
                .thenReturn(patient);
        Mockito.when(favoriteService.isFavorite(Mockito.eq(doctor), Mockito.eq(patient)))
                .thenReturn(true);


        // Execute
        patientService.addFavorite(user.getEmail(), license);
    }
}

