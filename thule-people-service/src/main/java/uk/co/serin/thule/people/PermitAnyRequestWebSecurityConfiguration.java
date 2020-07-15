package uk.co.serin.thule.people;

import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import uk.co.serin.thule.spring.boot.starter.resourceserver.autoconfiguration.ResourceServerAutoConfiguration;

@Configuration
@ConditionalOnProperty(value = "thule.peopleservice.permit-any-request-web-security-configuration-enabled")
@EnableWebSecurity
@EnableAutoConfiguration(exclude = {ManagementWebSecurityAutoConfiguration.class, OAuth2ResourceServerAutoConfiguration.class, SecurityAutoConfiguration.class,
        ResourceServerAutoConfiguration.class})
@Order(ResourceServerAutoConfiguration.ORDER - 1)
public class PermitAnyRequestWebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable().authorizeRequests().anyRequest().permitAll();
    }
}
