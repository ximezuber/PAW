package ar.edu.itba.paw.webapp.auth;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class TokenAuthenticationService {
    private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";
    private static final String ROLE_HEADER_NAME = "X-ROLE";

    @Autowired
    private TokenHandler tokenHandler;

    public void addAuthentication(HttpServletResponse response, Authentication authentication) {
        String token = tokenHandler.createTokenForUser(authentication.getName());
        UserDetails userDetails = tokenHandler.parseUserFromToken(token);
        Optional<? extends GrantedAuthority> authority = userDetails.getAuthorities().stream().findFirst();
        if (!authority.isPresent()) throw new AuthorizationServiceException("User " + authentication.getName() + " has no role");
        String role = authority.get().getAuthority();
        response.addHeader("Access-Control-Expose-Headers", AUTH_HEADER_NAME + ", " + ROLE_HEADER_NAME);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.addHeader(AUTH_HEADER_NAME, token);
        response.addHeader(ROLE_HEADER_NAME, role);
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        final String token = request.getHeader(AUTH_HEADER_NAME);
        if (token != null && tokenHandler.validateToken(token)) {
            final UserDetails user = tokenHandler.parseUserFromToken(token);
            if (user != null) {
                return new UserAuthentication(user);
            }
        }
        return null;
    }

    public String createTokenForUser(String email) {
        return tokenHandler.createTokenForUser(email);
    }

    public Map<String, String> readBodyForm(HttpServletRequest request) throws IOException {
        String body = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        body = java.net.URLDecoder.decode(body, request.getCharacterEncoding());
        Map<String, String> map = new HashMap<>();

        String[] pairs = body.split("&");

        for(String pair: pairs) {
            String[] split = pair.split("=");
            map.put(split[0], split[1]);
        }

        return map;
    }
}
