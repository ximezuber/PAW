package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.model.User;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User createUser(String firstName, String lastName, String password, String email){
        User user = new User(firstName, lastName, password, email);
        entityManager.persist(user);
        return user;
    }

    @Override
    public Optional<User> findUserByEmail(String email){
        User user = entityManager.find(User.class, email);
        return Optional.ofNullable(user);
    }

    @Override
    public void updateUser(User user, String firstName, String lastName, String password, String email) {
        User contextUser = entityManager.merge(user);
        if (password != null) {
            contextUser.setPassword(password);
        }
        contextUser.setEmail(email);
        contextUser.setFirstName(firstName);
        contextUser.setLastName(lastName);
        entityManager.persist(contextUser);
    }

    @Override
    public void deleteUser(User user) {
        User contextUser = entityManager.find(User.class, user.getEmail());
        entityManager.remove(contextUser);
    }

}
