package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Patient;
import ar.edu.itba.paw.model.Prepaid;
import ar.edu.itba.paw.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

//    @PersistenceContext
//    private EntityManager entityManager;

    @Autowired
    private PatientDaoImpl patientDao;

    @Test
    public void testCreate(){
        Patient patient = patientDao.create(id, prepaid.getName(), pripaidNumber, user2);

        assertNotNull(patient);
        assertEquals(user2.getEmail(), patient.getEmail());
        assertEquals(id, patient.getId());
        assertEquals(prepaid.getName(), patient.getPrepaid());
        assertEquals(pripaidNumber, patient.getPrepaidNumber());
        assertEquals(user2.getFirstName(), patient.getFirstName());
        assertEquals(user2.getLastName(), patient.getLastName());
    }

    @Test
    public void testGetPatientByEmail(){
        Patient patient = patientDao.getPatientByEmail(user.getEmail());

        assertNotNull(patient);
        assertEquals(user.getEmail(), patient.getEmail());
    }

}
