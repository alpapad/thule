package uk.co.serin.thule.spring.boot.starter.security.autoconfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.co.serin.thule.security.DelegatingSecurityContextHolder;

@Configuration
public class SecurityAutoConfiguration {
    @Bean
    public DelegatingSecurityContextHolder delegatingSecurityContextHolder() {
        return new DelegatingSecurityContextHolder();
    }
}
