package ar.edu.itba.paw.webapp.auth.jwt;

import ar.edu.itba.paw.webapp.auth.TokenAuthenticationService;
import ar.edu.itba.paw.webapp.auth.exceptions.InvalidTokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class StatelessAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private TokenAuthenticationService authenticationService;

    public StatelessAuthenticationFilter() {
        super("/**");
    }

    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        HttpServletResponse httpResponse = (HttpServletResponse) res;
        System.out.println("WebConfig; "+ httpRequest.getRequestURI());
//        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
//        httpResponse.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, X-AUTH-TOKEN, X-Requested-With, X-ROLE");
        httpResponse.setHeader("Access-Control-Max-Age", "3600");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Expose-Headers", "X-AUTH-TOKEN");
        httpResponse.addHeader("Access-Control-Expose-Headers", "X-ROLE");
        //httpResponse.addHeader("Access-Control-Expose-Headers", "observe");
        System.out.println("Request Method: "+httpRequest.getMethod());
        if (!(httpRequest.getMethod().equalsIgnoreCase("OPTIONS"))) {
            try {
                Authentication authentication = authenticationService.getAuthentication(httpRequest);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                chain.doFilter(req, res);
                SecurityContextHolder.getContext().setAuthentication(null);
            } catch(InvalidTokenException e) {
                ((HttpServletResponse) res).sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            }
        } else {
            System.out.println("Pre-flight");
            httpResponse.setHeader("Access-Control-Allow-Origin", "*");
            httpResponse.setHeader("Access-Control-Allow-Methods", "POST,GET,DELETE,PUT");
            //httpResponse.setHeader("Access-Control-Max-Age", "3600");
            httpResponse.setHeader("Access-Control-Allow-Headers", "Access-Control-Expose-Headers, "+"X-AUTH-TOKEN, content-type," +
                    "access-control-request-headers,access-control-request-method,accept,origin,x-auth-token,x-requested-with,responseType,x-role,token-header");
            httpResponse.setStatus(HttpServletResponse.SC_OK);
        }

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        Authentication authentication = authenticationService.getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
        SecurityContextHolder.getContext().setAuthentication(null);
    }

}
