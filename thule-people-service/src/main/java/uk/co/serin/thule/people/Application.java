package uk.co.serin.thule.people;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import uk.co.serin.thule.core.CoreConfiguration;
import uk.co.serin.thule.people.repository.support.ThuleJpaRepositoryFactoryBean;

@SpringBootApplication
@ComponentScan
@EnableDiscoveryClient
@EnableJpaAuditing
@EnableJpaRepositories(repositoryFactoryBeanClass = ThuleJpaRepositoryFactoryBean.class)
@Import(CoreConfiguration.class)
public class Application {
    Application() {
    }

    @SuppressWarnings("squid:S2095")
    // Suppress SonarQube bug 'Close this "ConfigurableApplicationContext"'
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}