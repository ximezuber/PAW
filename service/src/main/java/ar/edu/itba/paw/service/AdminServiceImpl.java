package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.AdminDao;
import ar.edu.itba.paw.interfaces.service.AdminService;
import ar.edu.itba.paw.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminDao adminDao;

    @Override
    public Admin getAdmin(String email) {
        return adminDao.getAdmin(email);
    }

    @Override
    public boolean isAdmin(String email) {
        return adminDao.isAdmin(email);
    }

}
