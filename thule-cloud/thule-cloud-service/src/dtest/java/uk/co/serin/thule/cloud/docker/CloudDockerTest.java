package uk.co.serin.thule.cloud.docker;

import com.gohenry.test.assertj.ActuatorUri;
import com.gohenry.utils.docker.DockerCompose;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;

import static com.gohenry.test.assertj.GoHenryAssertions.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
    public void _01_config_service_health_status_is_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(CONFIGURATION_SERVICE_BASE_URL + ACTUATOR_HEALTH));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }

    @Test
    public void _02_discovery_service_health_status_is_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(DISCOVERY_SERVICE_BASE_URL + ACTUATOR_HEALTH));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }

    @Test
    public void _03_admin_server_health_status_is_up() {
        // Given

        // When/Then
        assertThat(new ActuatorUri(URI.create(ADMIN_SERVICE_BASE_URL + ACTUATOR_HEALTH))).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }

    @Test
    public void _04_email_service_health_status_is_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(EMAIL_SERVICE_BASE_URL + ACTUATOR_HEALTH));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }

    @Test
    public void _05_people_service_health_status_is_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(PEOPLE_SERVICE_BASE_URL + ACTUATOR_HEALTH));

        // When/Then
        assertThat(actuatorUri).withHttpBasic("user", "user").waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }

    @Test
    public void _06_email_service_via_gateway_health_status_is_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(GATEWAY_SERVICE_BASE_URL + "/thule-email-service" + ACTUATOR_HEALTH));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }

    @Test
    public void _07_people_service_via_gateway_health_status_is_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(GATEWAY_SERVICE_BASE_URL + "/thule-people-service" + ACTUATOR_HEALTH));

        // When/Then
        assertThat(actuatorUri).withHttpBasic("user", "user").waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }

    @Test
    public void _08_gateway_service_health_status_is_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(GATEWAY_SERVICE_BASE_URL + ACTUATOR_HEALTH));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }

    @Test
    public void _09_admin_server_via_gateway_health_status_is_not_found() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(GATEWAY_SERVICE_BASE_URL + "/thule-admin-service" + ACTUATOR_HEALTH));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHttpStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    public void _10_config_service_via_gateway_health_status_is_not_found() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(GATEWAY_SERVICE_BASE_URL + "/thule-configuration-service" + ACTUATOR_HEALTH));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHttpStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    public void _11_discovery_service_via_gateway_health_status_is_not_found() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(GATEWAY_SERVICE_BASE_URL + "/thule-discovery-service" + ACTUATOR_HEALTH));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHttpStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    public void _12_gateway_service_via_gateway_health_status_is_not_found() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(GATEWAY_SERVICE_BASE_URL + "/thule-gateway-service" + ACTUATOR_HEALTH));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHttpStatus(HttpStatus.NOT_FOUND);
    }
}
