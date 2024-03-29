package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Specialty;
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
public class SpecialtyDaoImplTest {

    private static final String name = "specialty";

    private static final String name2 = "specialty2";


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
        Optional<Specialty> specialty = specialtyDao.getSpecialtyByName(name);

        assertTrue(specialty.isPresent());
        assertEquals(name, specialty.get().getSpecialtyName());

    }

    @Test
    public void testGetSpecialties(){
        List<Specialty> specialties = specialtyDao.getSpecialties();

        assertNotNull(specialties);
        assertEquals(1, specialties.size());

    }

}
