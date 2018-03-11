package uk.co.serin.thule.edge.docker;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.actuate.health.Status;

import uk.co.serin.thule.test.assertj.ActuatorUri;
import uk.co.serin.thule.utils.utils.DockerCompose;

import java.io.IOException;
import java.net.URI;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

public class DockerIntTest {
    private static final ActuatorUri EDGE_SERVER_STATUS_URI = new ActuatorUri(URI.create("http://docker-host:8080/actuator/health"));
    private static DockerCompose dockerCompose = new DockerCompose("src/itest/docker/thule-edge-server/docker-compose.yml");

    @BeforeClass
    public static void setUpClass() throws IOException {
        dockerCompose.downAndUp();
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        dockerCompose.down();
    }

    @Test
    public void edge_server_is_up() {
        assertThat(EDGE_SERVER_STATUS_URI).hasStatus(Status.UP);
    }
}