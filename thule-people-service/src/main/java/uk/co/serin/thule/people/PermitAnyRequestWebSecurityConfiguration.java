package uk.co.serin.thule.people;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import uk.co.serin.thule.spring.boot.starter.resourceserver.autoconfiguration.ResourceServerAutoConfiguration;

import lombok.Generated;

@Configuration
@EnableWebSecurity
@Generated
@Order(ResourceServerAutoConfiguration.ORDER - 1)
public class PermitAnyRequestWebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable().authorizeRequests().anyRequest().permitAll();
    }
}
