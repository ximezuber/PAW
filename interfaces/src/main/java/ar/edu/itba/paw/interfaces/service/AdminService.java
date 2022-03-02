package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.model.Admin;

public interface AdminService {

    Admin getAdmin(String email);

    boolean isAdmin(String email);
}
