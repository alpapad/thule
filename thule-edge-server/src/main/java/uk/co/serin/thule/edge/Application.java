package uk.co.serin.thule.edge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class Application {
    private static SpringApplication springApplication = new SpringApplication(Application.class);

    Application() {
    }

    public static void main(String[] args) {
        springApplication.run(args);
    }
}