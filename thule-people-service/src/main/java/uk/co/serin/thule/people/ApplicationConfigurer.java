package uk.co.serin.thule.people;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import uk.co.serin.thule.feign.EnableFeignJwtClients;
import uk.co.serin.thule.people.repository.support.ThuleJpaRepository;

import lombok.Generated;
import lombok.NoArgsConstructor;

@Configuration
@EnableAsync
@EnableDiscoveryClient
@EnableFeignJwtClients
@EnableJpaAuditing
@EnableJpaRepositories(repositoryBaseClass = ThuleJpaRepository.class)
@Generated
@NoArgsConstructor
@SuppressWarnings("squid:S1118") // Suppress Utility classes should not have public constructors
public class ApplicationConfigurer {
    static {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedMethods("*").allowedOrigins("*");
            }
        };
    }
}
