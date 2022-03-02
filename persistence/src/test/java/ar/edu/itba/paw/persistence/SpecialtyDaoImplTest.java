package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.Specialty;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Transactional
@Sql("classpath:schema.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class SpecialtyDaoImplTest {

    private static final String name = "specialty";

    private static final String name2 = "specialty2";

//    @PersistenceContext
//    private EntityManager entityManager;

    @Autowired
    private SpecialtyDaoImpl specialtyDao;

    @Test
    public void testCreate(){
        Specialty specialty = specialtyDao.createSpecialty(name2);

        assertNotNull(specialty);
        assertEquals(name2, specialty.getSpecialtyName());

    }

    @Test
    public void testGetSpecialtyByName(){
        Specialty specialty = specialtyDao.getSpecialtyByName(name);

        assertNotNull(specialty);
        assertEquals(name, specialty.getSpecialtyName());

    }

    @Test
    public void testGetSpecialties(){
        List<Specialty> specialties = specialtyDao.getSpecialties();

        assertNotNull(specialties);
        assertEquals(1, specialties.size());

    }

}
