package uk.co.serin.thule.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ComponentScan
@EnableAsync
public class Application {
    private static SpringApplication springApplication = new SpringApplication(Application.class);

    Application() {
    }

    public static void main(String[] args) {
        springApplication.run(args);
    }
}