package uk.co.serin.thule.spring.boot.starter.security.autoconfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.co.serin.thule.utils.security.DelegatingSecurityContextHolder;

@Configuration
public class SpringSecurityAutoConfiguration {
    @Bean
    public DelegatingSecurityContextHolder delegatingSecurityContextHolder() {
        return new DelegatingSecurityContextHolder();
    }
}
