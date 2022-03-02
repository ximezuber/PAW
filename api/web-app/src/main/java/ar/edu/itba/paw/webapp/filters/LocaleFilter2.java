package ar.edu.itba.paw.webapp.filters;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

public class LocaleFilter2 extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> language = Optional.ofNullable(request.getHeader("accept-language"));
        language.ifPresent(l -> Locale.setDefault(new Locale(l)));
        filterChain.doFilter(request, response);
    }
}
