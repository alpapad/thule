package uk.co.serin.thule.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import uk.co.serin.thule.authentication.keycloak.KeycloakProperties;
import uk.co.serin.thule.authentication.keycloak.KeycloakRepository;

import lombok.NoArgsConstructor;

@Configuration
@NoArgsConstructor
public class ApplicationConfigurer {
    @Bean
    public KeycloakRepository keycloakRepository(KeycloakProperties keycloakProperties) {
        return new KeycloakRepository(keycloakProperties);
    }

    @Bean
    public ClassLoaderTemplateResolver classLoaderTemplateResolver() {
        var classLoaderTemplateResolver = new ClassLoaderTemplateResolver();
        classLoaderTemplateResolver.setPrefix("templates/secrets/");
        classLoaderTemplateResolver.setSuffix(".yml");
        classLoaderTemplateResolver.setTemplateMode(TemplateMode.TEXT);
        return classLoaderTemplateResolver;
    }
}
