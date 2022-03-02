package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.model.User;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Map;


@Component
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User createUser(String firstName,String lastName, String password, String email){
        final User user = new User(firstName,lastName,password,email);
        entityManager.persist(user);
        return user;
    }

    @Override
    public User findUserByEmail(String email){
        return entityManager.find(User.class,email);
    }

    @Override
    public void updateUser(String email, Map<String, String> args){
        final Query query;
        if(!args.containsKey("password")) {
            query = entityManager.createQuery("update User as user set user.firstName = :firstName, user.lastName = :lastName where user.email = :email");
            query.setParameter("email", email);
            query.setParameter("firstName", args.get("firstName"));
            query.setParameter("lastName", args.get("lastName"));
            query.executeUpdate();
        }else{
            query = entityManager.createQuery("update User as user set user.firstName = :firstName, user.lastName = :lastName, user.password = :password where user.email = :email");
            query.setParameter("email", email);
            query.setParameter("firstName", args.get("firstName"));
            query.setParameter("lastName", args.get("lastName"));
            query.setParameter("password", args.get("password"));
            query.executeUpdate();
        }
    }

    @Override
    public long deleteUser(String email) {
        Query query = entityManager.createQuery("delete from User as us where us.email = :email");
        query.setParameter("email",email);
        return query.executeUpdate();
    }

}
