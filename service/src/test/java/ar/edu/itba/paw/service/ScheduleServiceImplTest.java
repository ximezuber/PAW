package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.ScheduleDao;
import ar.edu.itba.paw.interfaces.service.ClinicService;
import ar.edu.itba.paw.interfaces.service.DoctorClinicService;
import ar.edu.itba.paw.interfaces.service.DoctorService;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.ConflictException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleServiceImplTest {
    private final static User docUser = new User("fName", "lName",
            "password", "email@email.com");
    private final static Doctor doc = new Doctor(new Specialty("specialty"), "license",
            "phone", docUser);
    private final static Clinic clinic = new Clinic(0, "cName",
            "address", new Location("location"));
    private final static DoctorClinic dc = new DoctorClinic(doc, clinic, 100);
    private final static int day = 1;
    private final static int time = 10;

    @InjectMocks
    private ScheduleServiceImpl scheduleService = new ScheduleServiceImpl();

    @Mock
    private ScheduleDao scheduleDao;

    @Mock
    private DoctorClinicService doctorClinicService;

    @Mock
    private DoctorService doctorService;

    @Mock
    private ClinicService clinicService;

    @Test
    public void testCreate() throws ConflictException {
        // Set up
        Mockito.when(doctorClinicService.getDoctorClinic(Mockito.eq(doc.getLicense()),
                        Mockito.eq(clinic.getId())))
                .thenReturn(dc);
        Mockito.when(doctorService.getDoctorByEmail(Mockito.eq(doc.getEmail())))
                .thenReturn(doc);
        Mockito.when(scheduleDao.doctorHasSchedule(Mockito.eq(doc), Mockito.eq(day), Mockito.eq(time)))
                .thenReturn(false);
        Mockito.when(scheduleDao.createSchedule(Mockito.eq(day), Mockito.eq(time), Mockito.eq(dc)))
                .thenReturn(new Schedule(day, time, dc));

        // Execute
        Schedule schedule = scheduleService.createSchedule(time, day, doc.getEmail(), clinic.getId());

        // Assert
        Assert.assertEquals(doc.getLicense(), schedule.getDoctorClinic().getDoctor().getLicense());
        Assert.assertEquals(clinic.getId(), schedule.getDoctorClinic().getClinic().getId());
        Assert.assertEquals(day, schedule.getDay());
        Assert.assertEquals(time, schedule.getHour());
    }

    @Test(expected = ConflictException.class)
    public void testCreateExists() throws ConflictException {
        // Set up
        Mockito.when(doctorClinicService.getDoctorClinic(Mockito.eq(doc.getLicense()),
                        Mockito.eq(clinic.getId())))
                .thenReturn(dc);
        Mockito.when(doctorService.getDoctorByEmail(Mockito.eq(doc.getEmail())))
                .thenReturn(doc);
        Mockito.when(scheduleDao.doctorHasSchedule(Mockito.eq(doc), Mockito.eq(day), Mockito.eq(time)))
                .thenReturn(true);

        // Execute
        scheduleService.createSchedule(time, day, doc.getEmail(), clinic.getId());
    }
}
