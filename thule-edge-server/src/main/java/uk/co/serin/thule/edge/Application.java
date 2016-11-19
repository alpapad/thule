package uk.co.serin.thule.edge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class Application {
    Application() {
    }

    @SuppressWarnings("squid:S2095")
    // Suppress SonarQube bug 'Close this "ConfigurableApplicationContext"'
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}