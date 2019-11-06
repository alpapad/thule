package uk.co.serin.thule.configuration;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import uk.co.serin.thule.test.assertj.ActuatorUri;
import uk.co.serin.thule.utils.docker.DockerCompose;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigurationDockerTest {
    private static final String BASE_URL = "http://localhost:9888";
    private static final DockerCompose DOCKER_COMPOSE = new DockerCompose("src/dtest/docker/thule-configuration-service-dtests/docker-compose.yml");

    @BeforeClass
    public static void setUpClass() throws IOException {
        DOCKER_COMPOSE.downAndUp();
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        DOCKER_COMPOSE.down();
    }

    @Test
    public void given_docker_container_has_been_started_when_checking_health_then_status_is_up() {
        waitForTheApplicationToInitialize();
    }

    private void waitForTheApplicationToInitialize() {
        var actuatorUri = ActuatorUri.of(BASE_URL + "/actuator/health");
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }

    @Test
    public void given_docker_container_has_been_started_when_retrieving_actuator_info_then_it_shows_correct_microservice_is_in_docker_container() {
        // Given
        waitForTheApplicationToInitialize();

        // When
        var responseEntity = new RestTemplate().getForEntity(BASE_URL + "/actuator/info", Map.class);

        // Then
        assertThat(responseEntity.getBody()).contains(Map.entry("name", "thule-configuration-service"));
    }
}