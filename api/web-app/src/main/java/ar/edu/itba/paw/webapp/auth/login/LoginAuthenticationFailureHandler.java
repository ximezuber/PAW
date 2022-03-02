package ar.edu.itba.paw.webapp.auth.login;

import ar.edu.itba.paw.webapp.auth.exceptions.AlreadyLoggedInException;
import ar.edu.itba.paw.webapp.auth.exceptions.InvalidRequestException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        AuthenticationException e) {
        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
        if (e instanceof AlreadyLoggedInException) {
            httpServletResponse.setStatus(404);
        }if (e instanceof BadCredentialsException) {
            httpServletResponse.setStatus(401);
        } else if (e instanceof DisabledException) {
            httpServletResponse.setStatus(403);
        } else if (e instanceof InvalidRequestException){
            httpServletResponse.setStatus(422);
        } else {
            httpServletResponse.setStatus(409);
        }
    }
}
