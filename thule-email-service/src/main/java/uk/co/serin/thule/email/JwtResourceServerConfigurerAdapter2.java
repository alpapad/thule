package uk.co.serin.thule.email;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

import lombok.Generated;

@Configuration
@EnableResourceServer
@Generated
public class JwtResourceServerConfigurerAdapter2 extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
            .antMatchers("/apidocs/**").permitAll()
            .antMatchers("/emails/**").permitAll()
            .antMatchers("/**").authenticated().and().httpBasic().disable();
    }
}
