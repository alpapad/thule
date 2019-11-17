package uk.co.serin.thule.gateway.configuration;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

import uk.co.serin.thule.spring.boot.starter.security.oauth2.autoconfiguration.JwtResourceServerConfigurerAdapter;


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
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // allow OPTIONS urls to be unauthenticated (for pre-flight CORS requests)
            .antMatchers("/thule-*-service/**").permitAll() // allow micro-service urls to be unauthenticated (authentication is done in the down-stream micro-service)
            .antMatchers("/*/actuator/**").permitAll() // allow actuator urls to be unauthenticated (they are blocked by the load balancer so can not be reached from the internet )
            .antMatchers("/**").authenticated().and().httpBasic().disable(); // everything else must be authenticated, but not via basic authentication, i.e. we force OAuth2
    }
}
