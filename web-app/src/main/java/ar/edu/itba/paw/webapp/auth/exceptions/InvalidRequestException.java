package ar.edu.itba.paw.webapp.auth.exceptions;

import org.springframework.security.core.AuthenticationException;

public class InvalidRequestException extends AuthenticationException {
    public InvalidRequestException(String msg) {
        super(msg);
    }
}
