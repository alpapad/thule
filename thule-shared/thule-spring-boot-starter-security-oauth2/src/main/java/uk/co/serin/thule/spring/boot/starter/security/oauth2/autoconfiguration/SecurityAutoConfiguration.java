package uk.co.serin.thule.spring.boot.starter.security.oauth2.autoconfiguration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.co.serin.thule.security.oauth2.context.Oauth2DelegatingSecurityContextHolder;

@Configuration
public class SecurityAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public Oauth2DelegatingSecurityContextHolder delegatingSecurityContextHolder() {
        return new Oauth2DelegatingSecurityContextHolder();
    }
}
