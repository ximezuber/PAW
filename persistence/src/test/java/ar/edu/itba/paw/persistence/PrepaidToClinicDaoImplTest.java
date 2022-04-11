package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.Prepaid;
import ar.edu.itba.paw.model.PrepaidToClinic;
import org.junit.Assert;
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
public class PrepaidToClinicDaoImplTest {

    private static final Prepaid prepaid = new Prepaid("prepaid1");

    private static final Prepaid prepaid2 = new Prepaid("prepaid2");

    private static final Location location = new Location("location");

    private static final Clinic clinic = new Clinic(1, "clinic", "address", location);

    private static final Clinic clinic2 = new Clinic(2, "clinic2", "address2", location);

//    @PersistenceContext
//    private EntityManager entityManager;

    @Autowired
    private PrepaidToClinicDaoImpl prepaidToClinicDao;

    @Test
    public void testAddPrepaidToClinic(){
        PrepaidToClinic prepaidToClinic = prepaidToClinicDao.addPrepaidToClinic(prepaid2, clinic2);

        assertNotNull(prepaidToClinic);
        assertEquals(prepaid2.getName(), prepaidToClinic.getPrepaid().getName());
        assertEquals(clinic2.getId(), prepaidToClinic.getClinic().getId());
    }

    @Test
    public void testClinicHasPrepaid(){
        boolean bool1 = prepaidToClinicDao.clinicHasPrepaid(prepaid, clinic);
        boolean bool2 = prepaidToClinicDao.clinicHasPrepaid(prepaid, clinic2);

        Assert.assertTrue(bool1);
        Assert.assertFalse(bool2);
    }

}