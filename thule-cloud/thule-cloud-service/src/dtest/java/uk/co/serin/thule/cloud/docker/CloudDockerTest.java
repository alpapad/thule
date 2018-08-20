package uk.co.serin.thule.cloud.docker;

import com.gohenry.test.assertj.ActuatorUri;
import com.gohenry.utils.docker.DockerCompose;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.actuate.health.Status;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;

import static com.gohenry.test.assertj.GoHenryAssertions.assertThat;

public class CloudDockerTest {
    private static final String ACTUATOR_HEALTH = "/actuator/health";
    private static final String ADMIN_SERVICE_BASE_URL = "http://localhost:7092";
    private static final String CONFIGURATION_SERVICE_BASE_URL = "http://localhost:7888";
    private static final String DISCOVERY_SERVICE_BASE_URL = "http://localhost:7761";
    private static final DockerCompose DOCKER_COMPOSE = new DockerCompose("src/dtest/docker/thule-cloud-docker-tests/docker-compose.yml");
    private static final String EMAIL_SERVICE_BASE_URL = "http://localhost:7094";
    private static final String GATEWAY_SERVICE_BASE_URL = "http://localhost:7091";
    private static final String PEOPLE_SERVICE_BASE_URL = "http://localhost:7093";

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
    public void admin_server_health_status_is_up() {
        // Given

        // When/Then
        assertThat(new ActuatorUri(URI.create(ADMIN_SERVICE_BASE_URL + ACTUATOR_HEALTH))).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }

    @Test
    public void admin_server_via_gateway_health_status_is_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(GATEWAY_SERVICE_BASE_URL + "/thule-admin-service" + ACTUATOR_HEALTH));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }

    @Test
    public void config_service_health_status_is_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(CONFIGURATION_SERVICE_BASE_URL + ACTUATOR_HEALTH));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }

    @Test
    public void config_service_via_gateway_health_status_is_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(GATEWAY_SERVICE_BASE_URL + "/thule-configuration-service" + ACTUATOR_HEALTH));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }

    @Test
    public void discovery_service_health_status_is_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(DISCOVERY_SERVICE_BASE_URL + ACTUATOR_HEALTH));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }

    @Test
    public void email_service_health_status_is_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(EMAIL_SERVICE_BASE_URL + ACTUATOR_HEALTH));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }

    @Test
    public void email_service_via_gateway_health_status_is_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(GATEWAY_SERVICE_BASE_URL + "/thule-email-service" + ACTUATOR_HEALTH));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }

    @Test
    public void gateway_service_health_status_is_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(GATEWAY_SERVICE_BASE_URL + ACTUATOR_HEALTH));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }

    @Test
    public void people_service_health_status_is_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(PEOPLE_SERVICE_BASE_URL + ACTUATOR_HEALTH));

        // When/Then
        assertThat(actuatorUri).withHttpBasic("user", "user").waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }

    @Test
    public void people_service_via_gateway_health_status_is_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(GATEWAY_SERVICE_BASE_URL + "/thule-people-service" + ACTUATOR_HEALTH));

        // When/Then
        assertThat(actuatorUri).withHttpBasic("user", "user").waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }
}
