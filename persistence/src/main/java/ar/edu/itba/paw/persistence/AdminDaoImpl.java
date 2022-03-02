package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.AdminDao;
import ar.edu.itba.paw.model.Admin;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class AdminDaoImpl implements AdminDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Admin getAdmin(String email){
        return entityManager.find(Admin.class,email);
    }

    @Override
    public boolean isAdmin(String email){
        return getAdmin(email) != null;
    }
}

