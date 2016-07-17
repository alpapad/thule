package uk.co.serin.thule.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class Application {
    Application() {
    }

    @SuppressWarnings("squid:S2095") // Suppress SonarQube bug 'Close this "ConfigurableApplicationContext"'
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}