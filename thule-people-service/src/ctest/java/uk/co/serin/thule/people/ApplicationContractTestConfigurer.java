package uk.co.serin.thule.people;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import uk.co.serin.thule.security.oauth2.resourceserver.KeycloakUnsignedJwtDecoder;

@Configuration
public class ApplicationContractTestConfigurer {
//    @Bean
    public JwtDecoder jwtDecoder() {
        return new KeycloakUnsignedJwtDecoder();
    }
}
