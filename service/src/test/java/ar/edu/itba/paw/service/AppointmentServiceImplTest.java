package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.AppointmentDao;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class AppointmentServiceImplTest {
    private final static String license1 = "license";
    private final static Specialty specialty = new Specialty("specialty");
    private final static String phone = "12345678";
    private final static String firstName1 = "firstName1";
    private final static String firstName2 = "firstName2";
    private final static String lastName1 = "lastName1";
    private final static String lastName2 = "lastName2";
    private final static String password = "password";
    private final static String email1 = "email1@email.com";
    private final static String email2 = "email2@email.com";
    private final static int id1 = 0;
    private final static String clinic1 = "name1";
    private final static String address = "address";
    private final static Location location = new Location("location");
    private final static int price = 100;
    private final static LocalDateTime date = LocalDateTime.now().plusDays(10);
    private final static int year = date.getYear();
    private final static int month = date.getMonthValue();
    private final static int day = date.getDayOfMonth();
    private final static int time = date.getHour();
    User user;
    User user2;
    Doctor doc;
    Clinic clinic;
    DoctorClinic dc;
    List<Schedule> schedules;
    Schedule schedule;


    @InjectMocks
    AppointmentServiceImpl appointmentService = new AppointmentServiceImpl();

    @Mock
    AppointmentDao appointmentDao;

    @Mock
    DoctorService doctorService;

    @Mock
    UserService userService;

    @Mock
    EmailService emailService;

    @Mock
    private MessageSource messageSource;

    @Before
    public void setUp() {
        user = new User(firstName1, lastName1, password, email1);
        user2 = new User(firstName2, lastName2, password, email2);
        doc = new Doctor(specialty, license1, phone, user);
        clinic = new Clinic(id1, clinic1, address, location);
        dc = new DoctorClinic(doc, clinic, price);
        schedules = new ArrayList<>();
        schedule = new Schedule(date.getDayOfWeek().getValue(), time, dc);
        schedules.add(schedule);
        dc.setSchedule(schedules);

        Mockito.when(doctorService.getDoctorByLicense(Mockito.eq(license1)))
                .thenReturn(Optional.of(doc));
        Mockito.when(messageSource.getMessage(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn("message");
        Mockito.when(userService.isDoctor(email1)).thenReturn(true);

    }

    @Test
    public void testCreateAppointment()
            throws HasAppointmentException, DateInPastException, OutOfScheduleException,
            AppointmentAlreadyScheduledException {
        // Set up
        Appointment app = new Appointment(date, dc, user2);
        Mockito.when(appointmentDao.createAppointment(Mockito.eq(dc), Mockito.eq(user2), Mockito.any()))
                .thenReturn(app);
        Mockito.when(appointmentDao.getAppointment(Mockito.eq(doc), Mockito.any()))
                .thenReturn(Optional.empty());
        Mockito.when(appointmentDao.getAppointment(Mockito.eq(user2), Mockito.any()))
                .thenReturn(Optional.empty());

        // Execute
        Appointment result = appointmentService.createAppointment(dc, user2, year, month, day, time);

        // Assert
        Assert.assertEquals(license1, result.getDoctorClinic().getDoctor().getLicense());
        Assert.assertEquals(id1, result.getDoctorClinic().getClinic().getId());
        Assert.assertEquals(email2, result.getPatientUser().getEmail());
        Assert.assertEquals(year, result.getAppointmentKey().getDate().getYear());
        Assert.assertEquals(month, result.getAppointmentKey().getDate().getMonthValue());
        Assert.assertEquals(day, result.getAppointmentKey().getDate().getDayOfMonth());
        Assert.assertEquals(time, result.getAppointmentKey().getDate().getHour());
    }

    @Test(expected = DateInPastException.class)
    public void testPastDate()
            throws HasAppointmentException, DateInPastException, OutOfScheduleException,
            AppointmentAlreadyScheduledException {

        // Execute
        appointmentService.createAppointment(dc, user2, 2010, 1, 1, 10);
    }

    @Test(expected = OutOfScheduleException.class)
    public void testOutSchedule()
            throws HasAppointmentException, DateInPastException, OutOfScheduleException,
            AppointmentAlreadyScheduledException {
        // Set up
        Mockito.when(appointmentDao.getAppointment(Mockito.eq(doc), Mockito.any()))
                .thenReturn(Optional.empty());
        Mockito.when(appointmentDao.getAppointment(Mockito.eq(user2), Mockito.any()))
                .thenReturn(Optional.empty());

        LocalDateTime newDate = date.plusDays(1);

        // Execute
        appointmentService.createAppointment(dc, user2, newDate.getYear(), newDate.getMonthValue(),
                newDate.getDayOfMonth(), time);
    }

    @Test(expected = HasAppointmentException.class)
    public void testPatientHasApp()
            throws HasAppointmentException, DateInPastException, OutOfScheduleException,
            AppointmentAlreadyScheduledException {
        // Set up
        Mockito.when(appointmentDao.getAppointment(Mockito.eq(doc), Mockito.any()))
                .thenReturn(Optional.empty());
        Mockito.when(appointmentDao.getAppointment(Mockito.eq(user2), Mockito.any()))
                .thenReturn(Optional.of(new Appointment(date, dc, user2)));

        // Execute
        appointmentService.createAppointment(dc, user2, year, month, day, time);
    }

    @Test(expected = HasAppointmentException.class)
    public void testDocHasApp()
            throws HasAppointmentException, DateInPastException, OutOfScheduleException,
            AppointmentAlreadyScheduledException {
        // Set up
        Mockito.when(appointmentDao.getAppointment(Mockito.eq(user2), Mockito.any()))
                .thenReturn(Optional.empty());
        Mockito.when(appointmentDao.getAppointment(Mockito.eq(doc), Mockito.any()))
                .thenReturn(Optional.of(new Appointment(date, dc, user2)));
        // Execute
        appointmentService.createAppointment(dc, user2, year, month, day, time);
    }

    @Test
    public void testCancelAppPatient() throws EntityNotFoundException {
        // Set up
        LocalDateTime appDate = LocalDateTime.of(year, month, day, time, 0);
        Appointment app = new Appointment(appDate, dc, user2);
        Mockito.when(appointmentDao.getAppointment(Mockito.eq(doc), Mockito.eq(appDate)))
                .thenReturn(Optional.of(app));

        // Execute
        appointmentService.cancelUserAppointment(email2, license1, year, month, day, time);
    }

    @Test
    public void testCancelAppDoc() throws EntityNotFoundException {
        // Set up
        LocalDateTime appDate = LocalDateTime.of(year, month, day, time, 0);
        Appointment app = new Appointment(appDate, dc, user2);

        Mockito.when(appointmentDao.getAppointment(Mockito.eq(doc), Mockito.eq(appDate)))
                .thenReturn(Optional.of(app));

        // Execute
        appointmentService.cancelUserAppointment(email1, license1, year, month, day, time);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testCancelNoApp() throws EntityNotFoundException {
        // Set up
        LocalDateTime appDate = LocalDateTime.of(year, month, day, time, 0);

        Mockito.when(appointmentDao.getAppointment(Mockito.eq(doc), Mockito.eq(appDate)))
                .thenReturn(Optional.empty());
        Mockito.when(userService.isDoctor(email1)).thenReturn(true);

        // Execute
        appointmentService.cancelUserAppointment(email1, license1, year, month, day, time);
    }


}
