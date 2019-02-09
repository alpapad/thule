package uk.co.serin.thule.people;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

import uk.co.serin.thule.people.repository.support.ThuleJpaRepository;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Configuration
@EnableAsync
@EnableFeignClients
@EnableJpaRepositories(repositoryBaseClass = ThuleJpaRepository.class)
public class ApplicationConfigurer {
}
