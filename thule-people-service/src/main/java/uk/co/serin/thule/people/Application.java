package uk.co.serin.thule.people;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import uk.co.serin.thule.people.repository.support.ThuleJpaRepositoryFactoryBean;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableJpaAuditing
@EnableJpaRepositories(repositoryFactoryBeanClass = ThuleJpaRepositoryFactoryBean.class)
public class Application {
    private static SpringApplication springApplication = new SpringApplication(Application.class);

    Application() {
    }

    public static void main(String[] args) {
        springApplication.run(args);
    }
}