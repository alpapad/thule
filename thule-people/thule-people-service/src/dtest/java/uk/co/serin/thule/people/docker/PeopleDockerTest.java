package uk.co.serin.thule.people.docker;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.test.context.junit4.SpringRunner;

import uk.co.serin.thule.test.assertj.ActuatorUri;
import uk.co.serin.thule.utils.docker.DockerCompose;

import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.given;
import static org.awaitility.pollinterval.FibonacciPollInterval.fibonacci;
import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PeopleDockerTest {
    private static final String ACTUATOR_HEALTH_PATH = "/actuator/health";
    private static final String PEOPLE_PATH = "/people";
    private static DockerCompose dockerCompose = new DockerCompose("src/dtest/docker/thule-people-docker-tests/docker-compose.yml");
    @Autowired
    private Environment env;
    private String peopleServiceBaseUrl;
    private TestRestTemplate testRestTemplate = new TestRestTemplate();

    @BeforeClass
    public static void setUpClass() throws IOException {
        dockerCompose.downAndUp();
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        dockerCompose.down();
    }

    @Before
    public void setUp() {
        // Create base url
        String peopleServiceApiHost = env.getRequiredProperty("thule.peopleservice.api.host");
        int peopleServiceApiPort = env.getRequiredProperty("thule.peopleservice.api.port", Integer.class);
        peopleServiceBaseUrl = "http://" + peopleServiceApiHost + ":" + peopleServiceApiPort;
    }

    @Test
    public void when_checking_health_then_status_is_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(peopleServiceBaseUrl + ACTUATOR_HEALTH_PATH));

        // When/Then
        assertThat(actuatorUri).withHttpBasic("user", "user").waitingForMaximum(Duration.ofMinutes(5)).hasStatus(Status.UP);
    }

    @Test
    public void when_finding_all_people_then_at_least_one_person_is_found() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(peopleServiceBaseUrl + ACTUATOR_HEALTH_PATH));
        assertThat(actuatorUri).withHttpBasic("user", "user").waitingForMaximum(Duration.ofMinutes(5)).hasStatus(Status.UP);

        // When
        ResponseEntity<Map<String, Object>> personResponseEntity
                = testRestTemplate.withBasicAuth("user", "user").exchange(peopleServiceBaseUrl + PEOPLE_PATH, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<Map<String, Object>>() {
        });

        // Then
        Map embedded = (Map) Objects.requireNonNull(personResponseEntity.getBody()).get("_embedded");
        List people = (List) embedded.get("people");

        assertThat(personResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(people).isNotEmpty();
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
                        Connection connection = dataSource.getConnection();
                        JdbcUtils.closeConnection(connection);
                    });
            return true;
        }
    }
}