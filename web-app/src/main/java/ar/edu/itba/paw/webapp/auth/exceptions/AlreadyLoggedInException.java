package ar.edu.itba.paw.webapp.auth.exceptions;

import org.springframework.security.core.AuthenticationException;

public class AlreadyLoggedInException extends AuthenticationException {
    public AlreadyLoggedInException(String msg) {
        super(msg);
    }
}
