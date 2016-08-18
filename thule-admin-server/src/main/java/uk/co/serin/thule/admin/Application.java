package uk.co.serin.thule.admin;

import de.codecentric.boot.admin.config.EnableAdminServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableAdminServer
@EnableDiscoveryClient
public class Application {
    Application() {
    }

    @SuppressWarnings("squid:S2095") // Suppress SonarQube bug 'Close this "ConfigurableApplicationContext"'
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}