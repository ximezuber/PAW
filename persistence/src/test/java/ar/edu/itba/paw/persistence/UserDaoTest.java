package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Optional;

import static org.junit.Assert.*;

@Transactional
@Sql("classpath:schema.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserDaoTest {

    private static final String firstName = "firstName";

    private static final String lastName = "lastName";

    private static final String password = "password";

    private static final String email = "user@mail.com";

    private static final String firstName2 = "patFirstName";

    private static final String lastName2 = "patLastName";

    private static final String email2 = "patient@mail.com";


//    @PersistenceContext
//    private EntityManager entityManager;

     @Autowired
    private UserDaoImpl userDao;

    @Test
    public void testCreate() {
        User user = userDao.createUser(firstName, lastName, password, email);

        assertNotNull(user);
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(password, user.getPassword());

    }

    @Test
    public void testFindUserByEmail(){
        Optional<User> user = userDao.findUserByEmail(email2);

        assertTrue(user.isPresent());
        assertEquals(email2, user.get().getEmail());
        assertEquals(lastName2, user.get().getLastName());
        assertEquals(firstName2, user.get().getFirstName());

    }
}
