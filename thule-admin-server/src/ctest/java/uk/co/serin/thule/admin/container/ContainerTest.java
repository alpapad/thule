package uk.co.serin.thule.admin.container;

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
    private static final ActuatorUri ADMIN_SERVER_STATUS_URI = new ActuatorUri(URI.create("http://docker-host:8071/health"));
    private static DockerCompose dockerCompose = new DockerCompose("src/itest/docker/thule-admin-server/docker-compose.yml");

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
        assertThat(ADMIN_SERVER_STATUS_URI).waitingForMaximum(Duration.ofMinutes(5)).hasStatus(Status.UP);
    }
}
