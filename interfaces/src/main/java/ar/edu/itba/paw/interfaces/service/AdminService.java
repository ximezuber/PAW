package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.model.Admin;
import java.util.Optional;

public interface AdminService {

    Optional<Admin> getAdmin(String email);
}
