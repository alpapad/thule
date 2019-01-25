package uk.co.serin.thule.people.contract;

import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;

import org.awaitility.Duration;
import org.flywaydb.core.internal.jdbc.JdbcUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.context.annotation.Bean;

import java.sql.Connection;

import static org.awaitility.Awaitility.given;
import static org.awaitility.pollinterval.FibonacciPollInterval.fibonacci;

@TestConfiguration
public class RepositoryTestConfiguration {
    @Value("${wiremock.server.port}")
    private int wireMockServerPort;

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

    /**
     * When using a Feign client, it will try to use the load balancer (Ribbon) to lookup the
     * service via a discovery service (Eureka). However, for the integration test, we don't use
     * Ribbon or Eureka so we need to tell Feign to use a static server, in this case Wiremock.
     */
    @Bean
    public ServerList<Server> ribbonServerList() {
        return new StaticServerList<>(new Server("localhost", wireMockServerPort));
    }
}
