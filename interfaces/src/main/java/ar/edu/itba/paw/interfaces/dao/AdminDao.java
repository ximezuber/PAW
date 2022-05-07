package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Admin;

import java.util.Optional;

public interface AdminDao {

    Optional<Admin> getAdmin(String email);
}
