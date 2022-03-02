package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Admin;

public interface AdminDao {

    Admin getAdmin(String email);

    boolean isAdmin(String email);
}
