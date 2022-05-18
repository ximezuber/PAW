package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Doctor;
import ar.edu.itba.paw.model.Specialty;
import ar.edu.itba.paw.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Transactional
@Sql("classpath:schema.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class DoctorDaoImplTest {

    private static final Specialty specialty = new Specialty("specialty");

    private static final String license = "4";

    private static final String license2 = "1";

    private static final String phone = "1232214321";

    private static final User docUser = new User("docFirstName4", "docLastName4", "password", "doctor4@mail.com");

    private static final User user = new User("patFirstName", "patLastName", "password", "patient@mail.com");

    private static final User docUser2 = new User("docFirstName", "docLastName", "password", "doctor@mail.com");

    @Autowired
    private DoctorDaoImpl doctorDao;

    @Test
    public void testCreate(){
        Doctor doctor = doctorDao.createDoctor(specialty,license,phone,docUser);

        assertNotNull(doctor);
        assertEquals(docUser.getEmail(), doctor.getEmail());
        assertEquals(license, doctor.getLicense());
        assertEquals(specialty.getSpecialtyName(), doctor.getSpecialty().getSpecialtyName());
        assertEquals(phone, doctor.getPhoneNumber());

    }

    @Test
    public void testGetDoctors(){
        List<Doctor> doctors = doctorDao.getDoctors();

        assertNotNull(doctors);
        assertEquals(3, doctors.size());
    }

    @Test
    public void testGetDoctorByLicense(){
        Optional<Doctor> doctor = doctorDao.getDoctorByLicense(license2);

        assertTrue(doctor.isPresent());
        assertEquals(license2, doctor.get().getLicense());

    }

    @Test
    public void testIsDoctor(){
        Optional<Doctor> doc1 = doctorDao.getDoctorByEmail(docUser2.getEmail());
        Optional<Doctor> doc2 = doctorDao.getDoctorByEmail(user.getEmail());

        Assert.assertTrue(doc1.isPresent());
        Assert.assertFalse(doc2.isPresent());
    }

}
