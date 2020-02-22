package uk.co.serin.thule.spring.boot.starter.resourceserver.autoconfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.co.serin.thule.resourceserver.context.JwtUserAuthenticationSecurityContext;
import uk.co.serin.thule.security.context.DelegatingSecurityContextHolder;

@Configuration
public class Oauth2SecurityAutoConfiguration {
    @Bean
    public DelegatingSecurityContextHolder delegatingSecurityContextHolder() {
        return new DelegatingSecurityContextHolder();
    }

    @Bean
    public JwtUserAuthenticationSecurityContext jwtUserAuthenticationSecurityContext(DelegatingSecurityContextHolder delegatingSecurityContextHolder) {
        return new JwtUserAuthenticationSecurityContext(delegatingSecurityContextHolder);
    }
}
