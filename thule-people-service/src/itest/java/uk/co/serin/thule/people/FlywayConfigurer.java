package uk.co.serin.thule.people;

import org.awaitility.Duration;
import org.flywaydb.core.internal.util.jdbc.JdbcUtils;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;

import static org.awaitility.Awaitility.given;
import static org.awaitility.pollinterval.FibonacciPollInterval.fibonacci;

@Configuration
public class FlywayConfigurer {
    private static boolean flywayMigrated;

    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> {
            // Wait until the database is available because otherwise flyway migrate will fail
            // resulting in the application context not loading
            if (!flywayMigrated) {
                given().ignoreExceptions().pollInterval(fibonacci()).
                        await().timeout(Duration.FIVE_MINUTES).
                        untilAsserted(() -> {
                            Connection connection = JdbcUtils.openConnection(flyway.getDataSource());
                            JdbcUtils.closeConnection(connection);
                        });
                // Only migrate on the first test, otherwise if the application context is recreated
                // then the database is re migrated which can be time consuming
                flyway.migrate();
                flywayMigrated = true;
            }
        };
    }
}