package uk.co.serin.thule.people.docker;

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
    private static final ActuatorUri PEOPLE_SERVICE_STATUS_URI = new ActuatorUri(URI.create("http://docker-host:8090/actuator/health"));
    private static DockerCompose dockerCompose = new DockerCompose("src/itest/docker/thule-people-service/docker-compose.yml");

    @BeforeClass
    public static void setUpClass() throws IOException {
        dockerCompose.downAndUp();
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        dockerCompose.down();
    }

    @Test
    public void people_service_is_up() {
        assertThat(PEOPLE_SERVICE_STATUS_URI).withCredentials("user", "user").hasStatus(Status.UP);
    }
}
