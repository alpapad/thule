package uk.co.serin.thule.shared.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import uk.co.serin.thule.spring.boot.starter.oauth2jpa.autoconfiguration.OAuth2JpaIntegrationAutoConfiguration;

@SpringBootApplication(exclude = OAuth2JpaIntegrationAutoConfiguration.class)
public class Application {
    private static SpringApplication springApplication = new SpringApplication(Application.class);

    public static void main(String[] args) {
        springApplication.run(args);
    }
}