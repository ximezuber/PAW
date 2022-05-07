package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface UserService {
    User createUser(String firstName, String lastName, String password, String email);

    Optional<User> findUserByEmail(String email);

    boolean isDoctor(String email);

    boolean isAdmin(String email);

    void deleteUser(User user);

    void updateUser(User user, String email, String newPassword, String firstName, String lastName);
}
