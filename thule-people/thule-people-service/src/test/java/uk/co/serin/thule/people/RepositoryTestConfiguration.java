package uk.co.serin.thule.people;

import org.awaitility.Duration;
import org.flywaydb.core.internal.jdbc.JdbcUtils;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import uk.co.serin.thule.people.datafactory.TestDataFactory;

import java.sql.Connection;
import java.util.Optional;

import static org.awaitility.Awaitility.given;
import static org.awaitility.pollinterval.FibonacciPollInterval.fibonacci;

@EnableJpaAuditing
public class RepositoryTestConfiguration {
    @Bean
    public AuditorAware auditorAware() {
        return () -> Optional.of(TestDataFactory.JUNIT_TEST_USERNAME);
    }

    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> {
            // Wait until the database is available because otherwise flyway migrate will fail
            // resulting in the application context not loading
            given().ignoreExceptions().pollInterval(fibonacci()).
                    await().timeout(Duration.FIVE_MINUTES).
                           untilAsserted(() -> {
                               Connection connection = JdbcUtils.openConnection(flyway.getDataSource(), 1);
                               JdbcUtils.closeConnection(connection);
                           });
            flyway.migrate();
        };
    }
}
