package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface UserDao {
    User createUser(String firstName, String lastName, String password, String email);

    Optional<User> findUserByEmail(String email);

    void updateUser(User user, String firstName, String lastName, String password, String email);

    void deleteUser(User user);
}
