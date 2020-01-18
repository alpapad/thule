package uk.co.serin.thule.spring.boot.starter.security.oauth2.autoconfiguration;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import uk.co.serin.thule.security.oauth2.context.UserIdEnhancedUserAuthenticationConverter;
import uk.co.serin.thule.security.oauth2.resourceserver.JwtAccessTokenCustomizer;

import lombok.NoArgsConstructor;

@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnProperty(name = "thule.shared.oauth2.resource.enabled", matchIfMissing = true)
@Configuration
@EnableResourceServer
@NoArgsConstructor
public class ResourceServerAutoConfiguration {
    @Bean
    public JwtAccessTokenCustomizer jwtAccessTokenCustomizer(UserIdEnhancedUserAuthenticationConverter userIdEnhancedUserAuthenticationConverter) {
        return new JwtAccessTokenCustomizer(userIdEnhancedUserAuthenticationConverter);
    }

    @Bean
    public ResourceServerConfigurerAdapter resourceServerConfigurerAdapter() {
        return new ResourceServerConfigurerAdapter() {
            @Override
            public void configure(HttpSecurity httpSecurity) throws Exception {
                httpSecurity.cors() // allow pre-flight CORS requests
                            .and().authorizeRequests()
                            .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll() // allow actuator endpoints, even if not authenticated
                            .antMatchers("/swagger**").permitAll() // allow swagger docs, even if not authenticated
                            .antMatchers("/v2/api-docs/**").permitAll() // allow swagger docs, even if not authenticated
                            .antMatchers("/**").authenticated(); // everything else must be authenticated
            }
        };
    }
}
