package uk.co.serin.thule.gateway.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import lombok.NoArgsConstructor;

@Configuration
@NoArgsConstructor
public class MicroServiceDelegatingResourceServerConfigurerAdapter extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/thule-*-service/**").permitAll(); // allow micro-service urls to be unauthenticated (authentication is done in the down-stream micro-service)
    }
}
