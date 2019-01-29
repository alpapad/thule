package uk.co.serin.thule.people.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import uk.co.serin.thule.people.datafactory.TestDataFactory;

import java.util.Optional;

@EnableJpaAuditing
public class RepositoryIntTestConfiguration {
    @Bean
    public AuditorAware auditorAware() {
        return () -> Optional.of(TestDataFactory.JUNIT_TEST_USERNAME);
    }
}
