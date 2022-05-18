package ar.edu.itba.paw.webapp.helpers;

import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.User;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;


public class UserContextHelper {

    // Private constructor to prevent instantiation
    private UserContextHelper() {
        throw new UnsupportedOperationException();
    }

    public static String getLoggedUserEmail(SecurityContext context) {

        // principal refers to currently authenticated user
        Object principal = context.getAuthentication().getPrincipal();

        String email;
        if(principal instanceof UserDetails) {
            email = ((UserDetails)principal).getUsername();
        } else {
            email = principal.toString();
        }
        return email;
    }
}
