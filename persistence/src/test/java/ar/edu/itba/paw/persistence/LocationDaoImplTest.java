package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Location;
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
public class LocationDaoImplTest {

    private static final String name = "location";

    private static final String name2 = "location2";

    @Autowired
    private LocationDaoImpl locationDao;

    @Test
    public void testCreate(){
        Location location = locationDao.createLocation(name2);

        assertNotNull(location);
        assertEquals(name2, location.getLocationName());

    }

    @Test
    public void testGetLocationByName(){
        Optional<Location> location = locationDao.getLocationByName(name);

        assertTrue(location.isPresent());
        assertEquals(name, location.get().getLocationName());

    }

    @Test
    public void testGetLocations(){
        List<Location> locations = locationDao.getLocations();

        assertNotNull(locations);
        assertEquals(1, locations.size());

    }

}
