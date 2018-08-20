package com.gohenry.gateway.docker;

import com.gohenry.test.assertj.ActuatorUri;
import com.gohenry.utils.docker.DockerCompose;

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

import java.io.IOException;
import java.net.URI;
import java.time.Duration;

import static com.gohenry.test.assertj.GoHenryAssertions.assertThat;

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

    @Test
    public void health_status_is_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(gatewayBaseUrl + "/actuator/health"));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }

    @Before
    public void setUp() {
        // Create base url
        String gatewayApiHost = env.getRequiredProperty("thule.gatewayservice.api.host");
        int gatewayApiPort = env.getRequiredProperty("thule.gatewayservice.api.port", Integer.class);
        gatewayBaseUrl = String.format("http://%s:%s", gatewayApiHost, gatewayApiPort);
    }
}