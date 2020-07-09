package uk.co.serin.thule.email.contract;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.web.server.LocalServerPort;

import uk.co.serin.thule.test.assertj.ActuatorUri;

import java.time.Duration;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

class HealthCheckContractTest extends ContractBaseTest {
    @LocalServerPort
    private int port;

    @Test
    void when_checking_health_then_status_is_up() {
        // Given
        startEmbeddedSmtpServer();

        // Given
        var actuatorUri = ActuatorUri.using(String.format("http://localhost:%s/actuator/health", port));

        // When
        assertThat(actuatorUri)
                .waitingForMaximum(Duration.ofMinutes(5))

                // Then
                .hasHealthStatus(Status.UP);
    }
}
