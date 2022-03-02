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

import java.time.LocalDateTime;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Transactional
@Sql("classpath:schema.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class AppointmentDaoImplTest {

    private  static  final User user3 = new User("docFirstName", "docLastName", "password", "doctor@mail.com");

    private static final Doctor doc = new Doctor(new Specialty("specialty"), "1", "1234567890", user3);

    private static final Clinic clinic = new Clinic(1,"clinic", "address", new Location("location"));

    private static final DoctorClinic doctorClinic = new DoctorClinic(doc, clinic, 1);

    private static final User user = new User("patFirstName", "patLastName", "password", "patient@mail.com");

    private static final User user4 = new User("docFirstName2", "docLastName2", "password","doctor2@mail.com");

    private static final Doctor doc2 = new Doctor(new Specialty("specialty"), "2", "1234567890", user4);

    private static final DoctorClinic doctorClinic2 = new DoctorClinic(doc2, clinic, 1);

    private static final User user2 = new User("patFirstName2", "patLastName2", "password", "patient2@mail.com");

    private static final LocalDateTime cal = LocalDateTime.of(2019,10,1,8,0,0);


    @Autowired
    private AppointmentDaoImpl appointmentDao;

    @Test
    public void testCreate() {
        final Appointment appointment = appointmentDao.createAppointment(doctorClinic2, user2, cal);

        assertNotNull(appointment);
        assertEquals(doctorClinic2.getDoctor().getLicense(), appointment.getDoctorClinic().getDoctor().getLicense());
        assertEquals(doctorClinic2.getClinic().getId(), appointment.getDoctorClinic().getClinic().getId());
        assertEquals(user2.getEmail(), appointment.getPatientUser().getEmail());
        assertEquals(cal, appointment.getAppointmentKey().getDate());
    }

    @Test
    public void testHasAppointment(){

        final Appointment appointment = appointmentDao.hasAppointment(doctorClinic, cal);

        assertNotNull(appointment);
        assertEquals(doctorClinic.getDoctor().getLicense(), appointment.getDoctorClinic().getDoctor().getLicense());
        assertEquals(doctorClinic.getClinic().getId(), appointment.getDoctorClinic().getClinic().getId());
        assertEquals(cal, appointment.getAppointmentKey().getDate());

    }

    @Test
    public void testGetAllDoctorsAppointments(){
        final List<Appointment> apps = appointmentDao.getAllDoctorsAppointments(doc);
        assertNotNull(apps);
        Assert.assertTrue(!apps.isEmpty());
        assertEquals(doc.getLicense(), apps.get(0).getDoctorClinic().getDoctor().getLicense());

    }

    @Test
    public void testGetAllPatientsAppointments(){
        final List<Appointment> apps = appointmentDao.getPatientsAppointments(user);
        assertNotNull(apps);
        Assert.assertTrue(!apps.isEmpty());
        assertEquals(doc.getLicense(), apps.get(0).getDoctorClinic().getDoctor().getLicense());

    }


}
