package uk.co.serin.thule.cloud.container;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.actuate.health.Status;

import uk.co.serin.thule.test.assertj.ActuatorUri;
import uk.co.serin.thule.utils.docker.DockerCompose;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

public class ContainerTest {
    private static final String ACTUATOR_HEALTH = "/actuator/health";
    private static final String ADMIN_SERVER_BASE_URL = "http://172.17.0.1:9092";
    private static final String CONFIG_SERVICE_BASE_URL = "http://172.17.0.1:9888";
    private static final String DISCOVERY_SERVICE_BASE_URL = "http://172.17.0.1:9761";
    private static final DockerCompose DOCKER_COMPOSE = new DockerCompose("src/ctest/docker/thule-cloud-container-tests/docker-compose.yml");
    private static final String EDGE_SERVER_BASE_URL = "http://172.17.0.1:9091";
    private static final String EMAIL_SERVICE_BASE_URL = "http://172.17.0.1:9094";
    private static final String PEOPLE_SERVICE_BASE_URL = "http://172.17.0.1:9093";

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
        assertThat(new ActuatorUri(URI.create(ADMIN_SERVER_BASE_URL + ACTUATOR_HEALTH))).waitingForMaximum(Duration.ofMinutes(5)).hasStatus(Status.UP);
    }

    @Test
    public void admin_server_via_gateway_health_status_is_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(EDGE_SERVER_BASE_URL + "/thule-admin-server" + ACTUATOR_HEALTH));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasStatus(Status.UP);
    }

    @Test
    public void config_service_health_status_is_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(CONFIG_SERVICE_BASE_URL + ACTUATOR_HEALTH));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasStatus(Status.UP);
    }

    @Test
    public void config_service_via_gateway_health_status_is_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(EDGE_SERVER_BASE_URL + "/thule-config-service" + ACTUATOR_HEALTH));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasStatus(Status.UP);
    }

    @Test
    public void discovery_service_health_status_is_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(DISCOVERY_SERVICE_BASE_URL + ACTUATOR_HEALTH));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasStatus(Status.UP);
    }

    @Test
    public void edge_server_health_status_is_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(EDGE_SERVER_BASE_URL + ACTUATOR_HEALTH));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasStatus(Status.UP);
    }

    @Test
    public void email_service_health_status_is_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(EMAIL_SERVICE_BASE_URL + ACTUATOR_HEALTH));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasStatus(Status.UP);
    }

    @Test
    public void email_service_via_gateway_health_status_is_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(EDGE_SERVER_BASE_URL + "/thule-email-service" + ACTUATOR_HEALTH));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasStatus(Status.UP);
    }

    @Test
    public void people_service_health_status_is_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(PEOPLE_SERVICE_BASE_URL + ACTUATOR_HEALTH));

        // When/Then
        assertThat(actuatorUri).withCredentials("user", "user").waitingForMaximum(Duration.ofMinutes(5)).hasStatus(Status.UP);
    }

    @Test
    public void people_service_via_gateway_health_status_is_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(EDGE_SERVER_BASE_URL + "/thule-people-service" + ACTUATOR_HEALTH));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasStatus(Status.UP);
    }
}
