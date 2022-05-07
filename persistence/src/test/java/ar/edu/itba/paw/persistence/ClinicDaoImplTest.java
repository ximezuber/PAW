package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.Location;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Transactional
@Sql("classpath:schema.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ClinicDaoImplTest {

    private static final String name = "clinic";

    private static final int id = 1;

    private static final String name2 = "clinic3";

    private static final String address = "address";

    private static final String address2 = "address3";

    private static final Location location = new Location("location");


    @Autowired
    private ClinicDaoImpl clinicDao;

    @Test
    public void testCreate(){
        final Clinic clinic = clinicDao.createClinic(name2, address2, location);

        assertNotNull(clinic);
        assertEquals(name2, clinic.getName());
        assertEquals(address2, clinic.getAddress());
        assertEquals(location.getLocationName(), clinic.getLocation().getLocationName());

    }

    @Test
    public void testGetClinicById() throws Exception {
        final Clinic clinic = clinicDao.getClinicById(id).orElseThrow(Exception::new);

        assertNotNull(clinic);
        assertEquals(id, clinic.getId());

    }

    @Test
    public void testGetClinics() {
        List<Clinic> clinics = clinicDao.getClinics();

        assertNotNull(clinics);
        assertEquals(2, clinics.size());
    }

    @Test
    public void testGetClinicByLocation() {
        List<Clinic> clinics = clinicDao.getClinicsByLocation(location);

        assertNotNull(clinics);
        assertEquals(2, clinics.size());
    }

    @Test
    public void testClinicExists() {
        Optional<Clinic> clinic1 = clinicDao.getClinicByName(name);
        Optional<Clinic> clinic2 = clinicDao.getClinicByName(name2);

        Assert.assertTrue(clinic1.isPresent());
        assertEquals(name, clinic1.get().getName());
        assertEquals(location.getLocationName(), clinic1.get().getLocation().getLocationName());
        assertEquals(address, clinic1.get().getAddress());
        Assert.assertFalse(clinic2.isPresent());
    }

}
