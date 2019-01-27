package uk.co.serin.thule.people;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import uk.co.serin.thule.people.repository.support.ThuleJpaRepository;


@Configuration
@EnableFeignClients
@EnableJpaRepositories(repositoryBaseClass = ThuleJpaRepository.class)
public class ApplicationConfigurer {
}
