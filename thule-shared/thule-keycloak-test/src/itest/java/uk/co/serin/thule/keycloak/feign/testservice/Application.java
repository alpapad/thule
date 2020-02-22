package uk.co.serin.thule.keycloak.feign.testservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    private static SpringApplication springApplication = new SpringApplication(Application.class);

    public static void main(String[] args) {
        springApplication.run(args);
    }
}