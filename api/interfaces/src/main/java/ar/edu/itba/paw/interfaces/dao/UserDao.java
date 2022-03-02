package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.User;

import java.util.Map;

public interface UserDao {
    User createUser(String firstName,String lastName, String password, String email);

    User findUserByEmail(String email);

    void updateUser(String email, Map<String, String> args);

    long deleteUser(String email);
}
