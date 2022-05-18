package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.Prepaid;
import ar.edu.itba.paw.model.PrepaidToClinic;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
        Optional<PrepaidToClinic> bool1 = prepaidToClinicDao.getPrepaidToClinic(prepaid, clinic);
        Optional<PrepaidToClinic> bool2 = prepaidToClinicDao.getPrepaidToClinic(prepaid, clinic2);

        Assert.assertTrue(bool1.isPresent());
        Assert.assertFalse(bool2.isPresent());
    }

}