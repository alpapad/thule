package uk.co.serin.thule.discovery;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.web.client.TestRestTemplate;

import uk.co.serin.thule.test.assertj.ActuatorUri;

import java.time.Duration;
import java.util.Map;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

public class HealthCheckContractTest extends BaseContractTest {
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
