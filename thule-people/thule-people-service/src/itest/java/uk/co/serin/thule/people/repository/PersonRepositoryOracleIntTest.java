package uk.co.serin.thule.people.repository;

import org.awaitility.Duration;
import org.flywaydb.core.internal.util.jdbc.JdbcUtils;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Connection;

import static org.awaitility.Awaitility.given;
import static org.awaitility.pollinterval.FibonacciPollInterval.fibonacci;

/**
 * MySql, Oracle, H2 and HSQL embedded database drivers are on the itest classpath. By default, the H2
 * database will be configured for use by Spring Boot. However, this test is to test Oracle so
 * to disable the use of the test embedded database, use the following:
 *
 * <code>@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)</code>
 */
@ActiveProfiles({"itest", "itest-oracle"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PersonRepositoryOracleIntTest extends PersonRepositoryBaseIntTest {
    @TestConfiguration
    static class PersonRepositoryOracleIntTestConfiguration {
        @Bean
        public FlywayMigrationStrategy flywayMigrationStrategy() {
            return flyway -> {
                // Wait until the database is available because otherwise flyway migrate will fail
                // resulting in the application context not loading
                given().ignoreExceptions().pollInterval(fibonacci()).
                        await().timeout(Duration.FIVE_MINUTES).
                        untilAsserted(() -> {
                            Connection connection = JdbcUtils.openConnection(flyway.getDataSource());
                            JdbcUtils.closeConnection(connection);
                        });
                flyway.migrate();
            };
        }
    }
}