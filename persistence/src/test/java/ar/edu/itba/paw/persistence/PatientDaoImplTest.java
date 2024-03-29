package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Patient;
import ar.edu.itba.paw.model.Prepaid;
import ar.edu.itba.paw.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.Assert.*;

@Transactional
@Sql("classpath:schema.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class PatientDaoImplTest {

    private static final User user = new User("patFirstName", "patLastName", "password", "patient@mail.com");

    private static final User user2 = new User("patFirstName2", "patLastName2", "password", "patient2@mail.com");

    private static final String id = "332233";

    private static final Prepaid prepaid = new Prepaid("prepaid");

    private static final String pripaidNumber = "123231";

    @Autowired
    private PatientDaoImpl patientDao;

    @Test
    public void testCreate(){
        Patient patient = patientDao.create(id, prepaid, pripaidNumber, user2);

        assertNotNull(patient);
        assertEquals(user2.getEmail(), patient.getEmail());
        assertEquals(id, patient.getId());
        assertEquals(prepaid.getName(), patient.getPrepaid().getName());
        assertEquals(pripaidNumber, patient.getPrepaidNumber());
        assertEquals(user2.getFirstName(), patient.getFirstName());
        assertEquals(user2.getLastName(), patient.getLastName());
    }

    @Test
    public void testGetPatientByEmail(){
        Optional<Patient> patient = patientDao.getPatientByEmail(user.getEmail());

        assertTrue(patient.isPresent());
        assertEquals(user.getEmail(), patient.get().getEmail());
    }

}
