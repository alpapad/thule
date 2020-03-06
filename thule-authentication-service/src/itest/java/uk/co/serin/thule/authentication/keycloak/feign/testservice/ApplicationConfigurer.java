package uk.co.serin.thule.authentication.keycloak.feign.testservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.co.serin.thule.authentication.keycloak.KeycloakProperties;
import uk.co.serin.thule.authentication.keycloak.KeycloakRepository;
import uk.co.serin.thule.feign.EnableFeignJwtClients;

@Configuration
@EnableFeignJwtClients
public class ApplicationConfigurer {
    @Bean
    public KeycloakProperties keycloakProperties() {
        return new KeycloakProperties();
    }

    @Bean
    public KeycloakRepository keycloakRepository(KeycloakProperties keycloakProperties) {
        return new KeycloakRepository(keycloakProperties);
    }
}