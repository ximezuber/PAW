package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.AdminDao;
import ar.edu.itba.paw.interfaces.service.AdminService;
import ar.edu.itba.paw.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminDao adminDao;

    @Override
    public Optional<Admin> getAdmin(String email) {
        return adminDao.getAdmin(email);
    }

}
