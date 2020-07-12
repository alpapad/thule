package uk.co.serin.thule.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import uk.co.serin.thule.gateway.testcontainers.OpenIdMockServerContainer;
import uk.co.serin.thule.test.assertj.ActuatorUri;

import java.time.Duration;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@ActiveProfiles("ctest")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HealthCheckContractTest {
    @Container
    private static OpenIdMockServerContainer mockserver = new OpenIdMockServerContainer("5.11.0");
    @LocalServerPort
    private int port;

    @DynamicPropertySource
    public static void addDynamicProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("mock.server.ipaddress", mockserver::getContainerIpAddress);
        dynamicPropertyRegistry.add("mock.server.port", mockserver::getServerPort);
    }

    @Test
    void when_checking_health_then_status_is_up() {
        // Given
        var actuatorUri = ActuatorUri.using(String.format("http://localhost:%s/actuator/health", port));

        // When
        assertThat(actuatorUri)
                .waitingForMaximum(Duration.ofMinutes(5))

                // Then
                .hasHealthStatus(Status.UP);
    }
}
