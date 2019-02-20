package uk.co.serin.thule.gateway;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
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
    @Autowired
    private Environment env;
    private String gatewayBaseUrl;

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
        // Create base url
        String gatewayApiHost = env.getRequiredProperty("thule.gatewayservice.api.host");
        int gatewayApiPort = env.getRequiredProperty("thule.gatewayservice.api.port", Integer.class);
        gatewayBaseUrl = String.format("http://%s:%s", gatewayApiHost, gatewayApiPort);
    }

    @Test
    public void when_checking_health_then_status_is_up() {
        // Given
        ActuatorUri actuatorUri = ActuatorUri.of(gatewayBaseUrl + "/actuator/health");

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }
}