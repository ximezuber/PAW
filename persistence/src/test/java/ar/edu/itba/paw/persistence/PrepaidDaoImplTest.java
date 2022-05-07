package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Prepaid;
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
public class PrepaidDaoImplTest {

    private static final String name = "prepaid1";

    private static final String name2 = "prepaid3";

    @Autowired
    private PrepaidDaoImpl prepaidDao;

    @Test
    public void testCreate(){
        Prepaid prepaid = prepaidDao.createPrepaid(name2);

        assertNotNull(prepaid);
        assertEquals(name2, prepaid.getName());

    }

    @Test
    public void testGetSpecialtyByName(){
        Optional<Prepaid> prepaid = prepaidDao.getPrepaidByName(name);

        assertTrue(prepaid.isPresent());
        assertEquals(name, prepaid.get().getName());

    }

    @Test
    public void testGetSpecialties(){
        List<Prepaid> prepaid = prepaidDao.getPrepaid();

        assertNotNull(prepaid);
        assertEquals(2, prepaid.size());

    }

}




