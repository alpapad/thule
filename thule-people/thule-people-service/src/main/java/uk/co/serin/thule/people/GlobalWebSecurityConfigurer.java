package uk.co.serin.thule.people;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@ConditionalOnWebApplication
public class GlobalWebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and().
                // By default, users with the USER role only have GET access
                authorizeRequests().antMatchers("/**").hasRole("USER").and().
                // Disable csrf to allow the simple integration tests to pass
                csrf().disable();
    }
}