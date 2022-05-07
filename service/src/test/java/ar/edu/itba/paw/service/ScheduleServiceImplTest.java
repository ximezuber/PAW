package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.ScheduleDao;
import ar.edu.itba.paw.interfaces.service.DoctorClinicService;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.ConflictException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

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

    @Test
    public void testCreate() throws ConflictException {
        // Set up
        Mockito.when(scheduleDao.getDoctorScheduledHour(Mockito.eq(doc), Mockito.eq(day), Mockito.eq(time)))
                .thenReturn(Optional.empty());
        Mockito.when(scheduleDao.createSchedule(Mockito.eq(day), Mockito.eq(time), Mockito.eq(dc)))
                .thenReturn(new Schedule(day, time, dc));

        // Execute
        Schedule schedule = scheduleService.createSchedule(time, day, dc);

        // Assert
        Assert.assertEquals(doc.getLicense(), schedule.getDoctorClinic().getDoctor().getLicense());
        Assert.assertEquals(clinic.getId(), schedule.getDoctorClinic().getClinic().getId());
        Assert.assertEquals(day, schedule.getDay());
        Assert.assertEquals(time, schedule.getHour());
    }

    @Test(expected = ConflictException.class)
    public void testCreateExists() throws ConflictException {
        // Set up
        Mockito.when(scheduleDao.getDoctorScheduledHour(Mockito.eq(doc), Mockito.eq(day), Mockito.eq(time)))
                .thenReturn(Optional.of(new Schedule(day, time, dc)));

        // Execute
        scheduleService.createSchedule(time, day, dc);
    }
}
