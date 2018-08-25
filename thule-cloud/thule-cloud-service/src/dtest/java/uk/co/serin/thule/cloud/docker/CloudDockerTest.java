package uk.co.serin.thule.cloud.docker;

import com.gohenry.test.assertj.ActuatorUri;
import com.gohenry.utils.docker.DockerCompose;

import org.assertj.core.api.Assertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;

import static com.gohenry.test.assertj.GoHenryAssertions.assertThat;

public class CloudDockerTest {
    private static final String ACTUATOR_HEALTH = "/actuator/health";
    private static final String ADMIN_SERVICE_BASE_URL = "http://localhost:7093";
    private static final String CONFIGURATION_SERVICE_BASE_URL = "http://localhost:7888";
    private static final String DISCOVERY_SERVICE_BASE_URL = "http://localhost:7761";
    private static final DockerCompose DOCKER_COMPOSE = new DockerCompose("src/dtest/docker/thule-cloud-docker-tests/docker-compose.yml");
    private static final String EMAIL_SERVICE_BASE_URL = "http://localhost:7095";
    private static final String GATEWAY_SERVICE_BASE_URL = "http://localhost:7091";
    private static final String PEOPLE_SERVICE_BASE_URL = "http://localhost:7094";

    @BeforeClass
    public static void setUpClass() throws IOException {
        DOCKER_COMPOSE.command("pull");
        DOCKER_COMPOSE.downAndUp();
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        DOCKER_COMPOSE.down();
    }

    @Test
    public void when_checking_health_then_status_is_up() {
        // Services without going via the gateway
        assertThat(new ActuatorUri(URI.create(CONFIGURATION_SERVICE_BASE_URL + ACTUATOR_HEALTH))).waitingForMaximum(Duration.ofMinutes(5))
                                                                                                 .hasHealthStatus(Status.UP);
        assertThat(new ActuatorUri(URI.create(DISCOVERY_SERVICE_BASE_URL + ACTUATOR_HEALTH))).waitingForMaximum(Duration.ofMinutes(5))
                                                                                             .hasHealthStatus(Status.UP);
        assertThat(new ActuatorUri(URI.create(ADMIN_SERVICE_BASE_URL + ACTUATOR_HEALTH))).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
        assertThat(new ActuatorUri(URI.create(EMAIL_SERVICE_BASE_URL + ACTUATOR_HEALTH))).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
        assertThat(new ActuatorUri(URI.create(PEOPLE_SERVICE_BASE_URL + ACTUATOR_HEALTH))).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);

        // Services going via the gateway that should be up
        assertThat(new ActuatorUri(URI.create(GATEWAY_SERVICE_BASE_URL + "/thule-email-service" + ACTUATOR_HEALTH))).waitingForMaximum(Duration.ofMinutes(5))
                                                                                                                    .hasHealthStatus(Status.UP);
        assertThat(new ActuatorUri(URI.create(GATEWAY_SERVICE_BASE_URL + "/thule-people-service" + ACTUATOR_HEALTH))).waitingForMaximum(Duration.ofMinutes(5))
                                                                                                                     .hasHealthStatus(Status.UP);
        assertThat(new ActuatorUri(URI.create(GATEWAY_SERVICE_BASE_URL + ACTUATOR_HEALTH))).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);

        // Services going via the gateway that should be ignored
        try {
            assertThat(new ActuatorUri(URI.create(GATEWAY_SERVICE_BASE_URL + "/thule-admin-service" + ACTUATOR_HEALTH)))
                    .waitingForMaximum(Duration.ofSeconds(10)).hasHttpStatus(HttpStatus.NOT_FOUND);
        } catch (HttpClientErrorException e) {
            //Then
            Assertions.assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        try {
            assertThat(new ActuatorUri(URI.create(GATEWAY_SERVICE_BASE_URL + "/thule-configuration-service" + ACTUATOR_HEALTH)))
                    .waitingForMaximum(Duration.ofSeconds(10)).hasHttpStatus(HttpStatus.NOT_FOUND);
        } catch (HttpClientErrorException e) {
            //Then
            Assertions.assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        try {
            assertThat(new ActuatorUri(URI.create(GATEWAY_SERVICE_BASE_URL + "/thule-discovery-service" + ACTUATOR_HEALTH)))
                    .waitingForMaximum(Duration.ofSeconds(10)).hasHttpStatus(HttpStatus.NOT_FOUND);
        } catch (HttpClientErrorException e) {
            //Then
            Assertions.assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        try {
            assertThat(new ActuatorUri(URI.create(GATEWAY_SERVICE_BASE_URL + "/thule-gateway-service" + ACTUATOR_HEALTH)))
                    .waitingForMaximum(Duration.ofSeconds(10)).hasHttpStatus(HttpStatus.NOT_FOUND);
        } catch (HttpClientErrorException e) {
            //Then
            Assertions.assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }
}
