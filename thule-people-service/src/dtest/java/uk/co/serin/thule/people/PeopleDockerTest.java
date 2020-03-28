package uk.co.serin.thule.people;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import uk.co.serin.thule.test.assertj.ActuatorUri;

import java.io.File;
import java.time.Duration;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@SpringBootTest
@Testcontainers
public class PeopleDockerTest {
    private static String baseUrl;

    @Container
    private static DockerComposeContainer<?> dockerCompose =
            new DockerComposeContainer<>(new File("src/dtest/docker/docker-compose.yml"))
                    .withExposedService("thule-people-service_1", 8080);

    @DynamicPropertySource
    private static void mysqlProperties(DynamicPropertyRegistry registry) {
        baseUrl = String.format("http://localhost:%s", dockerCompose.getServicePort("thule-people-service_1", 8080));
    }

    @Test
    public void given_docker_container_has_been_started_when_checking_health_then_status_is_up() {
        waitForTheApplicationToInitialize();
    }

    private void waitForTheApplicationToInitialize() {
        var actuatorUri = ActuatorUri.using(baseUrl + "/actuator/health");
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }

    @Test
    public void given_docker_container_has_been_started_when_retrieving_actuator_info_then_it_shows_correct_microservice_is_in_docker_container() {
        // Given
        waitForTheApplicationToInitialize();

        // When
        WebTestClient.bindToServer().baseUrl(baseUrl).build()
                     .get().uri("/actuator/info")
                     .exchange()
                     .expectStatus().isOk()
                     .expectBody()

                     // Then
                     .jsonPath("$.name").isNotEmpty()
                     .jsonPath("$.name").isEqualTo("thule-people-service");
    }
}