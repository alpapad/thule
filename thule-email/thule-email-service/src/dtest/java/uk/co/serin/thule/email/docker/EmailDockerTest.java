package uk.co.serin.thule.email.docker;

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
import java.net.URI;
import java.time.Duration;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailDockerTest {
    private static DockerCompose dockerCompose = new DockerCompose("src/dtest/docker/thule-email-docker-tests/docker-compose.yml");

    private String emailServiceBaseUrl;

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

    @Before
    public void setUp() {
        // Create base url
        String emailServiceApiHost = env.getRequiredProperty("thule.emailservice.api.host");
        int emailServiceApiPort = env.getRequiredProperty("thule.emailservice.api.port", Integer.class);
        emailServiceBaseUrl = "http://" + emailServiceApiHost + ":" + emailServiceApiPort;
    }

    @Test
    public void when_checking_health_then_status_is_up() {
        // Given
        ActuatorUri actuatorUri = ActuatorUri.of(URI.create(emailServiceBaseUrl + "/actuator/health"));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }
}