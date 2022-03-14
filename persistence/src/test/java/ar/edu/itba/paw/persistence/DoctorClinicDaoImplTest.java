package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.print.Doc;
import java.util.List;

import static org.junit.Assert.*;

@Transactional
@Sql("classpath:schema.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class DoctorClinicDaoImplTest {

    private static final Location location = new Location("location");

    private static final Specialty specialty = new Specialty("specialty");

    private  static  final User user3 = new User("docFirstName", "docLastName", "password", "doctor@mail.com");

    private static final Doctor doc = new Doctor( specialty, "1", "1234567890", user3);

    private static final Clinic clinic = new Clinic(1,"clinic", "address", location);

    private  static  final User user4 = new User("docFirstName3", "docLastName3", "password", "doctor3@mail.com");


    private static final Doctor doc2 = new Doctor(specialty, "3", "1234", user4);

    private static final int consultPrice = 1;

//    @PersistenceContext
//    private EntityManager entityManager;

    @Autowired
    private DoctorClinicDaoImpl doctorClinicDao;

    @Test
    public void testCreate(){
        DoctorClinic doctorClinic = doctorClinicDao.createDoctorClinic(doc2, clinic, consultPrice);

        assertNotNull(doctorClinic);
        assertEquals(doc2.getLicense(), doctorClinic.getDoctor().getLicense());
        assertEquals(clinic.getId(), doctorClinic.getClinic().getId());
        assertEquals(consultPrice, doctorClinic.getConsultPrice());

    }

    @Test
    public void testGetDoctorClinicsForDoctor(){
        List<DoctorClinic> doctorClinics = doctorClinicDao.getDoctorsSubscribedClinics(doc);

        assertNotNull(doctorClinics);
        assertEquals(1, doctorClinics.size());
        assertEquals(doc.getLicense(), doctorClinics.get(0).getDoctor().getLicense());
        assertEquals(clinic.getId(), doctorClinics.get(0).getClinic().getId());

    }

    @Test
    public void testGetFilteredDoctorsByLocation(){
        List<Doctor> doctorClinics = doctorClinicDao.getFilteredDoctorClinics(location, new Specialty(""),
                "", "", new Prepaid(""), 0);

        assertNotNull(doctorClinics);
        assertEquals(1, doctorClinics.size());
    }

    @Test
    public void testGetFilteredDoctorsBySpecialty(){
        List<Doctor> doctors = doctorClinicDao.getFilteredDoctorClinics(new Location(""), specialty,
                "", "", new Prepaid(""), 0);

        assertNotNull(doctors);
        assertEquals(1, doctors.size());
        assertEquals(specialty.getSpecialtyName(), doctors.get(0).getSpecialty().getSpecialtyName());

    }

    @Test
    public void testGetFilteredDoctorsByFirstName(){
        List<Doctor> doctors = doctorClinicDao.getFilteredDoctorClinics(new Location(""), new Specialty(""),
                doc.getFirstName(), "", new Prepaid(""), 0);

        assertNotNull(doctors);
        assertEquals(1, doctors.size());
        assertEquals(doc.getFirstName(), doctors.get(0).getFirstName());

    }

    @Test
    public void testGetFilteredDoctorsByLastName(){
        List<Doctor> doctors = doctorClinicDao.getFilteredDoctorClinics(new Location(""), new Specialty(""),
                "", doc.getLastName(), new Prepaid(""), 0);

        assertNotNull(doctors);
        assertEquals(1, doctors.size());
        assertEquals(doc.getLastName(), doctors.get(0).getLastName());

    }

    @Test
    public void testGetFilteredDoctorsByConsultPrice(){
        List<Doctor> doctors = doctorClinicDao.getFilteredDoctorClinics(new Location(""), new Specialty(""),
                "", "", new Prepaid(""), consultPrice);

        assertNotNull(doctors);
        assertEquals(1, doctors.size());

    }

}
