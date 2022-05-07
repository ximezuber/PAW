package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.service.AdminService;
import ar.edu.itba.paw.interfaces.service.DoctorService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    public User createUser(String firstName, String lastName, String password, String email) {
        return userDao.createUser(firstName, lastName, password, email);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userDao.findUserByEmail(email);
    }

    @Override
    public boolean isDoctor(String email) {
        return doctorService.getDoctorByEmail(email).isPresent();
    }

    @Override
    public boolean isAdmin(String email) {
        return adminService.getAdmin(email).isPresent();
    }

    @Transactional
    @Override
    public void deleteUser(User user) {
        userDao.deleteUser(user);
    }

    @Transactional
    @Override
    public void updateUser(User user, String email, String newPassword, String firstName, String lastName) {
        firstName = firstName == null || firstName.equals("") ? user.getFirstName() : firstName;
        lastName = lastName == null || lastName.equals("") ? user.getLastName() : lastName;
        email = email == null || email.equals("") ? user.getEmail() : email;
        userDao.updateUser(user, firstName, lastName, newPassword, email);
    }

}
