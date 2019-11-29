package uk.co.serin.thule.spring.boot.starter.data.autoconfiguration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.co.serin.thule.data.audit.SpringSecurityAuditorAware;
import uk.co.serin.thule.security.oauth2.context.DelegatingSecurityContextHolder;

@ConditionalOnClass({DelegatingSecurityContextHolder.class})
@Configuration
public class DataAutoConfiguration {
    @Bean
    public SpringSecurityAuditorAware springSecurityAuditorAware(DelegatingSecurityContextHolder delegatingSecurityContextHolder) {
        return new SpringSecurityAuditorAware(delegatingSecurityContextHolder);
    }
}
