package uk.co.serin.thule.spring.boot.starter.security.autoconfiguration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.co.serin.thule.security.context.DelegatingSecurityContextHolder;

@Configuration
public class SecurityAutoConfiguration {
    @Bean
    @ConditionalOnMissingClass("uk.co.serin.thule.security.oauth2.context.Oauth2DelegatingSecurityContextHolder")
    public DelegatingSecurityContextHolder delegatingSecurityContextHolder() {
        return new DelegatingSecurityContextHolder();
    }
}
