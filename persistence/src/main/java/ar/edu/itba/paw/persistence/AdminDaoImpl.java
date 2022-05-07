package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.AdminDao;
import ar.edu.itba.paw.model.Admin;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class AdminDaoImpl implements AdminDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Admin> getAdmin(String email){
        Admin admin =  entityManager.find(Admin.class, email);
        return Optional.ofNullable(admin);
    }
}

