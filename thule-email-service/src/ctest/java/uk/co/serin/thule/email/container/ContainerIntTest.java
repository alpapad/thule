package uk.co.serin.thule.email.container;

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

public class ContainerIntTest {
    private static final ActuatorUri EMAIL_SERVER_STATUS_URI = new ActuatorUri(URI.create("http://docker-host:8091/actuator/health"));
    private static DockerCompose dockerCompose = new DockerCompose("src/ctest/docker/thule-email-service/docker-compose.yml");

    @BeforeClass
    public static void setUpClass() throws IOException {
        dockerCompose.downAndUp();
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        dockerCompose.down();
    }

    @Test
    public void admin_server_is_up() {
        assertThat(EMAIL_SERVER_STATUS_URI).waitingForMaximum(Duration.ofMinutes(5)).hasStatus(Status.UP);
    }
}
