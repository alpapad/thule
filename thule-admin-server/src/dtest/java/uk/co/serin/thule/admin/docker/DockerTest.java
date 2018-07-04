package uk.co.serin.thule.admin.docker;

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
import org.springframework.test.context.junit4.SpringRunner;

import uk.co.serin.thule.test.assertj.ActuatorUri;
import uk.co.serin.thule.utils.docker.DockerCompose;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DockerTest {
    private static final String ACTUATOR_HEALTH = "/actuator/health";
    private static DockerCompose dockerCompose = new DockerCompose("src/dtest/docker/thule-admin-server-docker-tests/docker-compose.yml");

    private String adminServiceBaseUrl;

    @Autowired
    private Environment env;

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
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(adminServiceBaseUrl + ACTUATOR_HEALTH));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasStatus(Status.UP);
    }

    @Before
    public void setUp() {
        // Create base url
        String adminServiceApiHost = env.getRequiredProperty("thule.adminservice.api.host");
        int adminServiceApiPort = env.getRequiredProperty("thule.adminservice.api.port", Integer.class);
        adminServiceBaseUrl = "http://" + adminServiceApiHost + ":" + adminServiceApiPort;
    }

    @TestConfiguration
    static class ContainerTestConfiguration {
    }
}