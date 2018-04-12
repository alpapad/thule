package uk.co.serin.thule.people.container;

import org.flywaydb.core.internal.util.jdbc.JdbcUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import uk.co.serin.thule.test.assertj.ActuatorUri;
import uk.co.serin.thule.utils.docker.DockerCompose;

import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.time.Duration;

import javax.sql.DataSource;

import static org.awaitility.Awaitility.given;
import static org.awaitility.pollinterval.FibonacciPollInterval.fibonacci;
import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@ActiveProfiles({"ctest", "ctest-client", "${spring.profiles.include:default}"})
@DataJpaTest
@Commit
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
public class ContainerTest {
    private static DockerCompose dockerCompose = new DockerCompose("src/ctest/docker/thule-people-service/docker-compose.yml");
    private ActuatorUri actuatorUri;
    @Autowired
    private Environment env;
    private String peopleServiceUrlPrefix;

    @BeforeClass
    public static void setUpClass() throws IOException {
        dockerCompose.downAndUp();
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        dockerCompose.down();
    }

    @Test
    public void people_service_is_up() {
        assertThat(actuatorUri).withCredentials("user", "user").waitingForMaximum(Duration.ofMinutes(5)).hasStatus(Status.UP);
    }

    @Before
    public void setUp() {
        String peopleServiceApiHost = env.getRequiredProperty("thule.peopleservice.api.host");
        int peopleServiceApiPort = env.getRequiredProperty("thule.peopleservice.api.port", Integer.class);
        peopleServiceUrlPrefix = "http://" + peopleServiceApiHost + ":" + peopleServiceApiPort;
        actuatorUri = new ActuatorUri(URI.create(peopleServiceUrlPrefix + "/actuator/health"));
    }

    /**
     * Test configuration to delay the tests until the database is available
     */
    @TestConfiguration
    static class ContainerTestConfiguration {
        @Bean
        public boolean databaseAvailable(DataSource dataSource) {
            given().ignoreExceptions().pollInterval(fibonacci()).
                    await().timeout(org.awaitility.Duration.FIVE_MINUTES).
                    untilAsserted(() -> {
                        Connection connection = JdbcUtils.openConnection(dataSource);
                        JdbcUtils.closeConnection(connection);
                    });
            return true;
        }
    }
}
