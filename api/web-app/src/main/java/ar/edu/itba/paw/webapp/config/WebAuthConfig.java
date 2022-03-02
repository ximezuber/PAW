package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.*;
import ar.edu.itba.paw.webapp.auth.jwt.JwtAuthenticationEntryPoint;
import ar.edu.itba.paw.webapp.auth.jwt.PawAuthenticationFailureHandler;
import ar.edu.itba.paw.webapp.auth.jwt.PawUrlStatelessAuthenticationSuccessHandler;
import ar.edu.itba.paw.webapp.auth.jwt.StatelessAuthenticationFilter;
import ar.edu.itba.paw.webapp.auth.login.LoginAuthenticationFailureHandler;
import ar.edu.itba.paw.webapp.auth.login.LoginAuthenticationFilter;
import ar.edu.itba.paw.webapp.auth.login.LoginAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
@PropertySources({
        @PropertySource(value = "classpath:authKey.properties"),
        @PropertySource(value = "classpath:env/secret.properties", ignoreResourceNotFound = false)
})

@ComponentScan("ar.edu.itba.paw.webapp.auth")
public class WebAuthConfig extends WebSecurityConfigurerAdapter {
    public static final BCryptPasswordEncoder B_CRYPT_PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Autowired
    private Environment environment;

    @Autowired
    private PawUserDetailsService userDetailsService;

    @Autowired
    private LoginAuthenticationSuccessHandler loginAuthenticationSuccessHandler;

    @Autowired
    private LoginAuthenticationFailureHandler loginAuthenticationFailureHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override protected void configure(final HttpSecurity http) throws Exception{
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/doctors/**").permitAll()
                .antMatchers(HttpMethod.GET, "/specialties").permitAll()
                .antMatchers(HttpMethod.GET, "/locations").permitAll()
                .antMatchers(HttpMethod.GET, "/clinics/**").permitAll()
                .antMatchers(HttpMethod.GET, "/prepaids").permitAll()
                .antMatchers(HttpMethod.GET, "/appointments/available/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/login").anonymous()
                .antMatchers(HttpMethod.POST, "/api/patients").permitAll()
                .antMatchers(HttpMethod.POST, "/locations").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/locations/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/specialties").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/specialties/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/prepaids").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/prepaids/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/clinics/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/clinics/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/clinics/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/patients/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/patients/**").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/doctors/*/image").hasAnyRole("ADMIN", "DOCTOR")
                .antMatchers(HttpMethod.POST, "/doctors").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/doctors/**").hasRole("DOCTOR")
                .antMatchers(HttpMethod.PUT, "/doctors/**").hasRole("DOCTOR")
                .antMatchers(HttpMethod.DELETE, "/doctors/**").hasAnyRole("DOCTOR", "ADMIN")
                .antMatchers("/appointments/**").hasAnyRole("USER", "DOCTOR")
                //.antMatchers(HttpMethod.GET).authenticated()
                .antMatchers(HttpMethod.POST).authenticated()
                .antMatchers(HttpMethod.DELETE).authenticated()
                .antMatchers(HttpMethod.PUT).authenticated()
                .antMatchers("/**").permitAll()
                .and()
                .formLogin().usernameParameter("email").passwordParameter("password")
                .loginProcessingUrl("/api/login").successHandler(myAuthenticationSuccessHandler())
                .failureHandler(myAuthenticationFailureHandler())
                .and()
                .addFilterBefore(loginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(userDetailsService)
                .exceptionHandling().authenticationEntryPoint(authEntryPoint())
                .and()
                .exceptionHandling().accessDeniedHandler(myAccessDeniedHandler()).and()
                .csrf().disable();
    }

    @Override public void configure(final WebSecurity web) {
        web.ignoring().antMatchers("/index.html","/css/**", "/js/**", "/img/**", "/favicon.ico", "/403");
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return B_CRYPT_PASSWORD_ENCODER;
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new PawUrlStatelessAuthenticationSuccessHandler();
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public StatelessAuthenticationFilter authFilter() {
        StatelessAuthenticationFilter filter = new StatelessAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(new PawUrlStatelessAuthenticationSuccessHandler());
        filter.setAuthenticationFailureHandler(new PawAuthenticationFailureHandler());
        return filter;
    }

    @Bean
        public LoginAuthenticationFilter loginFilter() throws Exception {
        LoginAuthenticationFilter filter = new LoginAuthenticationFilter();
        filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login",
                "POST"));
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(loginAuthenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(loginAuthenticationFailureHandler);
        return filter;
    }

    @Bean
    public RequestMatcher protectedEndpointsMatcher() {
        return new OrRequestMatcher(
                new AntPathRequestMatcher("/**", "GET"),
                new AntPathRequestMatcher("/**", "POST"),
                new AntPathRequestMatcher("/**", "PUT"),
                new AntPathRequestMatcher("/**", "DELETE"),
                new AntPathRequestMatcher("/**", "PATCH")
        );
    }

    @Bean
    public AuthenticationEntryPoint authEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }

    @Bean
    public AuthenticationFailureHandler myAuthenticationFailureHandler() {
        return new PawAuthenticationFailureHandler();
    }

    @Bean
    public AccessDeniedHandler myAccessDeniedHandler() {
        return new PawAccessDeniedHandler();
    }
}
