package uk.co.serin.thule.discovery;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import uk.co.serin.thule.test.assertj.ActuatorUri;
import uk.co.serin.thule.utils.docker.DockerCompose;

import java.io.IOException;
import java.time.Duration;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DiscoveryDockerTest {
    private static final DockerCompose DOCKER_COMPOSE = new DockerCompose("src/dtest/docker/docker-compose.yml");
    @Value("${thule.discoveryservice.api.host}")
    private String discoveryServiceApiHost;
    @Value("${thule.discoveryservice.api.port}")
    private int discoveryServiceApiPort;
    private String discoveryServiceBaseUrl;

    @BeforeClass
    public static void setUpClass() throws IOException {
        DOCKER_COMPOSE.downAndUp();
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        DOCKER_COMPOSE.down();
    }

    @Before
    public void setUp() {
        discoveryServiceBaseUrl = String.format("http://%s:%s", discoveryServiceApiHost, discoveryServiceApiPort);
    }

    @Test
    public void when_checking_health_then_status_is_up() {
        // Given
        var actuatorUri = ActuatorUri.of(discoveryServiceBaseUrl + "/actuator/health");

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }
}