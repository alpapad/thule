package uk.co.serin.thule.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    private static SpringApplication springApplication = new SpringApplication(Application.class);

    Application() {
    }

    public static void main(String[] args) {
        springApplication.run(args);
    }
}