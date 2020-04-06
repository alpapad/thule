package uk.co.serin.thule.admin;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import uk.co.serin.thule.test.assertj.ActuatorUri;

import java.time.Duration;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@ActiveProfiles("ctest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HealthCheckContractTest {
    @LocalServerPort
    private int port;

    @Test
    public void when_checking_health_then_status_is_up() {
        // Given
        var actuatorUri = ActuatorUri.using(String.format("http://localhost:%s/actuator/health", port));

        // When
        assertThat(actuatorUri)
                .waitingForMaximum(Duration.ofMinutes(5))

                // Then
                .hasHealthStatus(Status.UP);
    }
}
