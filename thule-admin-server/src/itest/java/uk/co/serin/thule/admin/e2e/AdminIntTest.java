package uk.co.serin.thule.admin.e2e;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import uk.co.serin.thule.test.assertj.ActuatorUri;

import java.net.URI;
import java.time.Duration;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;


@ActiveProfiles({"itest", "${spring.profiles.include:default}"})
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminIntTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void is_status_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(restTemplate.getRootUri() + "/actuator/health"));

        // When/Then
        assertThat(actuatorUri).withCredentials("user", "user").waitingForMaximum(Duration.ofMinutes(5)).hasStatus(Status.UP);
    }
}
