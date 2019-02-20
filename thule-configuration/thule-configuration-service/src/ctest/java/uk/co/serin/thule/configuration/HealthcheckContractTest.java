package uk.co.serin.thule.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import uk.co.serin.thule.test.assertj.ActuatorUri;

import java.time.Duration;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@ActiveProfiles("ctest")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HealthcheckContractTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void when_checking_health_then_status_is_up() {
        // Given
        var actuatorUri = ActuatorUri.of(restTemplate.getRootUri() + "/actuator/health");

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }
}