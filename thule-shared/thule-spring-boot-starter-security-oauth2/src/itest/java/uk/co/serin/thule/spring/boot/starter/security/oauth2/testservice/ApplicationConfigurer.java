package uk.co.serin.thule.spring.boot.starter.security.oauth2.testservice;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import uk.co.serin.thule.spring.boot.starter.security.oauth2.autoconfiguration.Oauth2AutoConfiguration;

@Configuration
@Import(Oauth2AutoConfiguration.class)
public class ApplicationConfigurer {
}
