package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.CustomPermissionEvaluator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@ComponentScan("ar.edu.itba.paw.webapp.auth")
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Bean
    public PermissionEvaluator myPermissionEvaluator() {
        return new CustomPermissionEvaluator();
    }

    @Override
    public MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(myPermissionEvaluator());
        return expressionHandler;
    }
}
