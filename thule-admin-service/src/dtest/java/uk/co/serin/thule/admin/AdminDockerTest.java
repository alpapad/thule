package uk.co.serin.thule.admin;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import uk.co.serin.thule.test.assertj.ActuatorUri;
import uk.co.serin.thule.utils.docker.DockerCompose;

import java.io.IOException;
import java.time.Duration;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminDockerTest {
    private static final String BASE_URL = "http://localhost:9093";
    private static final DockerCompose DOCKER_COMPOSE = new DockerCompose("src/dtest/docker/thule-admin-service-dtests/docker-compose.yml");

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
        var actuatorUri = ActuatorUri.using(BASE_URL + "/actuator/health");
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }

    @Test
    public void given_docker_container_has_been_started_when_retrieving_actuator_info_then_it_shows_correct_microservice_is_in_docker_container() {
        // Given
        waitForTheApplicationToInitialize();

        // When
        WebTestClient.bindToServer().baseUrl(BASE_URL).build()
                     .get().uri("/actuator/info")
                     .exchange()
                     .expectStatus().isOk()
                     .expectBody()

                     // Then
                     .jsonPath("$.name").isNotEmpty()
                     .jsonPath("$.name").isEqualTo("thule-admin-service");
    }
}