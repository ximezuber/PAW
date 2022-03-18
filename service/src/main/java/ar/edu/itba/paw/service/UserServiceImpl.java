package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.service.AdminService;
import ar.edu.itba.paw.interfaces.service.DoctorService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AdminService adminService;

    @Autowired
    private DoctorService doctorService;

    @Transactional
    @Override
    public User createUser(String firstName,String lastName, String password, String email) {
        return userDao.createUser(firstName, lastName, password, email);
    }

    @Override
    public User findUserByEmail(String email) {
        return userDao.findUserByEmail(email);
    }

    @Override
    public boolean userExists(String email) {
        return (userDao.findUserByEmail(email) != null);
    }

    @Override
    public boolean isDoctor(String email) {
        return doctorService.isDoctor(email);
    }

    @Override
    public boolean isAdmin(String email) {
        return adminService.isAdmin(email);
    }

    @Transactional
    @Override
    public long deleteUser(String email) {
        return userDao.deleteUser(email);
    }

    @Transactional
    @Override
    public void updateUser(String email, String newPassword, String firstName, String lastName) {
        Map<String,String> args = new HashMap<>();
        if(newPassword != null){
            args.put("password", newPassword);
        }
        if(!firstName.equals("")){
            args.put("firstName", firstName);
        }
        if(!lastName.equals("")){
            args.put("lastName", lastName);
        }
        userDao.updateUser(email, args);
    }

}
