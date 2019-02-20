package uk.co.serin.thule.gateway;

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

@SpringBootTest
@RunWith(SpringRunner.class)
public class GatewayDockerTest {
    private static DockerCompose dockerCompose = new DockerCompose("src/dtest/docker/thule-gateway-docker-tests/docker-compose.yml");
    private String gatewayBaseUrl;
    @Value("${thule.gatewayservice.api.host}")
    private String gatewayServiceApiHost;
    @Value("${thule.gatewayservice.api.port}")
    private int gatewayServiceApiPort;

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
        gatewayBaseUrl = String.format("http://%s:%s", gatewayServiceApiHost, gatewayServiceApiPort);
    }

    @Test
    public void when_checking_health_then_status_is_up() {
        // Given
        var actuatorUri = ActuatorUri.of(gatewayBaseUrl + "/actuator/health");

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }
}