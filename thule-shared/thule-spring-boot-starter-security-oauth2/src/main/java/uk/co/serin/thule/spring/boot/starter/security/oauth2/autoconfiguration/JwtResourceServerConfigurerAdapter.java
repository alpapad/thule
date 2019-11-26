package uk.co.serin.thule.spring.boot.starter.security.oauth2.autoconfiguration;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

@Configuration
@EnableResourceServer
public class JwtResourceServerConfigurerAdapter extends ResourceServerConfigurerAdapter {
    private DefaultTokenServices tokenServices;

    public JwtResourceServerConfigurerAdapter(DefaultTokenServices tokenServices) {
        this.tokenServices = tokenServices;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer config) {
        config.tokenServices(tokenServices);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // allow OPTIONS urls to be unauthenticated (for pre-flight CORS requests)
            .antMatchers("/apidocs/**").permitAll() // allow urls for apidocs to be unauthenticated
            .antMatchers("/**").authenticated().and().httpBasic().disable(); // everything else must be authenticated, but not via basic authentication, i.e. we force OAuth2
    }
}
