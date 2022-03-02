package ar.edu.itba.paw.webapp.auth.login;

import ar.edu.itba.paw.webapp.auth.exceptions.AlreadyLoggedInException;
import ar.edu.itba.paw.webapp.auth.TokenAuthenticationService;
import ar.edu.itba.paw.webapp.auth.exceptions.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;
    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        if (tokenAuthenticationService.getAuthentication(request) != null) {
            throw new AlreadyLoggedInException("already-logged-in");
        }

        Map<String, String> body = null;
        try {
             body = tokenAuthenticationService.readBodyForm(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (body == null) throw new InvalidRequestException("invalid-body");

        String email = body.get(EMAIL);
        String password = body.get(PASSWORD);

        if (email == null || password == null) throw new InvalidRequestException("invalid-body");

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email,
                password);

        this.setDetails(request, token);
        return this.getAuthenticationManager().authenticate(token);

    }

}
