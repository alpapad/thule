package uk.co.serin.thule.security.oauth2.resourceserver;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import lombok.NoArgsConstructor;

@Configuration
@NoArgsConstructor
public class JwtResourceServerConfigurerAdapter extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // allow OPTIONS urls to be unauthenticated (for pre-flight CORS requests)
            .antMatchers("/**").authenticated().and().httpBasic().disable(); // everything else must be authenticated, but not via basic authentication, i.e. we force OAuth2
    }
}
