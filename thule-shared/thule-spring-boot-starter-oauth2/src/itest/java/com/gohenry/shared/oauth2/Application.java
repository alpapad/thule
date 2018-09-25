package com.gohenry.shared.oauth2;

import com.gohenry.spring.boot.starter.oauth2jpa.autoconfiguration.OAuth2JpaIntegrationAutoConfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = OAuth2JpaIntegrationAutoConfiguration.class)
public class Application {
    private static SpringApplication springApplication = new SpringApplication(Application.class);

    public static void main(String[] args) {
        springApplication.run(args);
    }
}