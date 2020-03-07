package uk.co.serin.thule.people;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.context.SecurityContextHolder;

import uk.co.serin.thule.feign.EnableFeignJwtClients;
import uk.co.serin.thule.people.repository.support.ThuleJpaRepository;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Configuration
@EnableAsync
@EnableDiscoveryClient
@EnableFeignJwtClients
@EnableJpaAuditing
@EnableJpaRepositories(repositoryBaseClass = ThuleJpaRepository.class)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationConfigurer {
    static {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }
}
