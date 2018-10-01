package uk.co.serin.thule.gateway.configuration;

import uk.co.serin.thule.spring.boot.starter.oauth2.autoconfiguration.JwtResourceServerConfigurerAdapter;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

@Configuration
@EnableResourceServer
public class MicroServiceDelegatingJwtResourceServerConfigurerAdapter extends JwtResourceServerConfigurerAdapter {
    public MicroServiceDelegatingJwtResourceServerConfigurerAdapter(DefaultTokenServices tokenServices) {
        super(tokenServices);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                .antMatchers("/thule-*-service/**").permitAll()
                .antMatchers("/*/actuator/**").permitAll()
                .antMatchers("/**").authenticated().and().httpBasic().disable();
    }
}
