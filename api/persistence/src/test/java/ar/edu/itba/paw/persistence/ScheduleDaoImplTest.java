package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Transactional
@Sql("classpath:schema.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ScheduleDaoImplTest {

    private static final User user = new User("docFirstName", "docLastName", "password","doctor@mail.com");
    private static final Doctor doc = new Doctor(new Specialty("specialty"), "1", "1234567890", user);

    private static final Clinic clinic = new Clinic(1,"clinic", "address", new Location("location"));

    private static final DoctorClinic doctorClinic = new DoctorClinic(doc, clinic, 1);

    private static final int day = 3;

    private static final int hour = 8;

    private static final int day2 = 4;

    private static final int hour2 = 10;

//    @PersistenceContext
//    private EntityManager entityManager;

    @Autowired
    private ScheduleDaoImpl scheduleDao;


    @Test
    public void testCreate(){
        Schedule schedule = scheduleDao.createSchedule(day2, hour2, doctorClinic);

        assertNotNull(schedule);
        assertEquals(day2, schedule.getDay());
        assertEquals(hour2, schedule.getHour());

    }

    @Test
    public void testGetDoctorClinicSchedule(){
        List<Schedule> schedules = scheduleDao.getDoctorClinicSchedule(doctorClinic);

        assertNotNull(schedules);
        assertEquals(1, schedules.size());
    }

    @Test
    public void testHasSchedule(){
        boolean bool1 = scheduleDao.doctorHasSchedule(doctorClinic.getDoctor(), day, hour);
        boolean bool2 = scheduleDao.doctorHasSchedule(doctorClinic.getDoctor(), day2, hour2);

        Assert.assertTrue(bool1);
        Assert.assertFalse(bool2);

    }

}
