package uk.co.serin.thule.edge.container;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import uk.co.serin.thule.test.assertj.ActuatorUri;
import uk.co.serin.thule.utils.docker.DockerCompose;

import java.io.IOException;
import java.net.URI;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@ActiveProfiles({"ctest", "${spring.profiles.include:default}"})
@RunWith(SpringRunner.class)
@SpringBootTest
public class ContainerTest {
    private static DockerCompose dockerCompose = new DockerCompose("src/ctest/docker/thule-edge-server-container-tests/docker-compose.yml");

    @Autowired
    private Environment env;

    private String edgeServerBaseUrl;

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
        String edgeServerApiHost = env.getRequiredProperty("thule.edgeserver.api.host");
        int edgeServerApiPort = env.getRequiredProperty("thule.edgeserver.api.port", Integer.class);
        edgeServerBaseUrl = "http://" + edgeServerApiHost + ":" + edgeServerApiPort;
    }

    @Test
    public void health_status_is_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(edgeServerBaseUrl + "/actuator/health"));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(java.time.Duration.ofMinutes(5)).hasStatus(Status.UP);
    }

    @TestConfiguration
    static class ContainerTestConfiguration {
    }
}