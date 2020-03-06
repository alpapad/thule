package uk.co.serin.thule.authentication.keycloak.resourceserver.testservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.co.serin.thule.authentication.keycloak.KeycloakProperties;
import uk.co.serin.thule.authentication.keycloak.KeycloakRepository;

@Configuration
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