package uk.co.serin.thule.people.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
public class RepositoryIntTestConfiguration {
    private static final String MOCK_USERS_NAME = "user";

    @Bean
    public AuditorAware auditorAware() {
        return () -> Optional.of(MOCK_USERS_NAME);
    }
}
