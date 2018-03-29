package uk.co.serin.thule.discovery.docker;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.actuate.health.Status;

import uk.co.serin.thule.test.assertj.ActuatorUri;
import uk.co.serin.thule.utils.utils.DockerCompose;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

public class ContainerTest {
    private static final ActuatorUri DISCOVERY_SERVER_STATUS_URI = new ActuatorUri(URI.create("http://docker-host:8761/actuator/health"));
    private static DockerCompose dockerCompose = new DockerCompose("src/itest/docker/thule-discovery-service/docker-compose.yml");

    @BeforeClass
    public static void setUpClass() throws IOException {
        dockerCompose.downAndUp();
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        dockerCompose.down();
    }

    @Test
    public void discovery_service_is_up() {
        assertThat(DISCOVERY_SERVER_STATUS_URI).waitingForMaximum(Duration.ofMinutes(5)).hasStatus(Status.UP);
    }
}