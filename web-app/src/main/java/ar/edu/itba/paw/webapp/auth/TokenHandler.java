package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.webapp.auth.exceptions.InvalidTokenException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class TokenHandler {

    @Autowired
    private PawUserDetailsService userService;

    private final String secret;

    @Autowired
    public TokenHandler(Environment environment) {
        this.secret = environment.getRequiredProperty("JWT.secret");
    }

    public UserDetails parseUserFromToken(String token) {
        String email = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return userService.loadUserByUsername(email);
    }

    public String createTokenForUser(String email) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + TimeUnit.DAYS.toMillis(90l));
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

    }

    public boolean validateToken(String token) {

        if(!Jwts.parser().setSigningKey(secret).isSigned(token)) {
            return false;
        }

        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("Expired or invalid JWT token") {
            };
        }
    }
}
