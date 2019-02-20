package uk.co.serin.thule.email.contract;

import org.junit.Test;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.web.server.LocalServerPort;

import uk.co.serin.thule.test.assertj.ActuatorUri;

import java.net.URI;
import java.time.Duration;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

public class HealthcheckContractTest extends ContractBaseTest {
    @LocalServerPort
    private int port;

    @Test
    public void when_checking_health_then_status_is_up() {
        // Given
        startEmbeddedSmtpServer();

        var actuatorUri = ActuatorUri.of(String.format("http://localhost:%s/actuator/health", port));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }
}
