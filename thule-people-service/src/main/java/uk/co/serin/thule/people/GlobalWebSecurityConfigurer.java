package uk.co.serin.thule.people;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@ConditionalOnWebApplication
@Order(SecurityProperties.BASIC_AUTH_ORDER + 1) // Before default order specified in Spring boot class ApplicationWebSecurityConfigurerAdapter
public class GlobalWebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(WebSecurity web) {
        // By default, the OPTIONS method with Spring Security has to have credentials but in a
        // javascript pre-flight request, there are no credentials so we need to turn off security
        // for OPTIONS
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }
}
