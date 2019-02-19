package uk.co.serin.thule.admin;

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
import java.net.URI;
import java.time.Duration;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminDockerTest {
    private static DockerCompose dockerCompose = new DockerCompose("src/dtest/docker/thule-admin-docker-tests/docker-compose.yml");
    @Value("${thule.adminservice.api.host}")
    private String adminServiceApiHost;
    @Value("${thule.adminservice.api.port}")
    private int adminServiceApiPort;
    private String adminServiceBaseUrl;

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
        adminServiceBaseUrl = String.format("http://%s:%s", adminServiceApiHost, adminServiceApiPort);
    }

    @Test
    public void when_checking_health_then_status_is_up() {
        // Given
        var actuatorUri = new ActuatorUri(URI.create(adminServiceBaseUrl + "/actuator/health"));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }
}