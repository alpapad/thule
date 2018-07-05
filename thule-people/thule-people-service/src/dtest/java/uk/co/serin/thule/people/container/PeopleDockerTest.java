package uk.co.serin.thule.people.container;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import uk.co.serin.thule.test.assertj.ActuatorUri;
import uk.co.serin.thule.utils.docker.DockerCompose;

import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.given;
import static org.awaitility.pollinterval.FibonacciPollInterval.fibonacci;
import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PeopleDockerTest {
    private static final String ACTUATOR_HEALTH = "/actuator/health";
    private static final String URL_FOR_PEOPLE = "/people";
    private static DockerCompose dockerCompose = new DockerCompose("src/dtest/docker/thule-people-docker-tests/docker-compose.yml");
    @Autowired
    private Environment env;
    private String peopleServiceBaseUrl;
    private RestTemplate restTemplate = new RestTemplate();

    @BeforeClass
    public static void setUpClass() throws IOException {
        dockerCompose.downAndUp();
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        dockerCompose.down();
    }

    @Test
    public void get_all_people() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(peopleServiceBaseUrl + ACTUATOR_HEALTH));
        assertThat(actuatorUri).withHttpBasic("user", "user").waitingForMaximum(Duration.ofMinutes(5)).hasStatus(Status.UP);

        // When
        ResponseEntity<Map<String, Object>> personResponseEntity
                = restTemplate.exchange(peopleServiceBaseUrl + URL_FOR_PEOPLE, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<Map<String, Object>>() {
        });

        // Then
        Map<String, String> embedded = Map.class.cast(personResponseEntity.getBody().get("_embedded"));
        List<String> people = List.class.cast(embedded.get("people"));

        assertThat(personResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(people).isNotEmpty();
    }

    @Test
    public void health_status_is_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(peopleServiceBaseUrl + ACTUATOR_HEALTH));

        // When/Then
        assertThat(actuatorUri).withHttpBasic("user", "user").waitingForMaximum(Duration.ofMinutes(5)).hasStatus(Status.UP);
    }

    @Before
    public void setUp() {
        // Create base url
        String peopleServiceApiHost = env.getRequiredProperty("thule.peopleservice.api.host");
        int peopleServiceApiPort = env.getRequiredProperty("thule.peopleservice.api.port", Integer.class);
        peopleServiceBaseUrl = "http://" + peopleServiceApiHost + ":" + peopleServiceApiPort;

        // Add credentials to rest template
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        interceptors = new ArrayList<>(interceptors);
        interceptors.removeIf(BasicAuthorizationInterceptor.class::isInstance);
        interceptors.add(new BasicAuthorizationInterceptor("user", "user"));
        restTemplate.setInterceptors(interceptors);
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