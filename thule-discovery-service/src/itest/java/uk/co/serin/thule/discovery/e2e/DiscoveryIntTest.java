package uk.co.serin.thule.discovery.e2e;

import org.awaitility.Duration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import uk.co.serin.thule.test.assertj.ActuatorUri;

import java.net.URI;
import java.util.Map;

import static org.awaitility.Awaitility.given;
import static org.awaitility.pollinterval.FixedPollInterval.fixed;
import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles({"itest", "${spring.profiles.include:default}"})
@RunWith(SpringRunner.class)
public class DiscoveryIntTest {
    @Autowired
    private ActuatorClient actuatorClient;
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void can_invoke_a_service_via_discovery() {
        // Given
        given().ignoreExceptions().pollInterval(fixed(Duration.FIVE_SECONDS)).
                await().timeout(Duration.FIVE_MINUTES).
                untilAsserted(() -> {
                    assertThat(discoveryClient.getServices()).contains("thule-discovery-service");
                });

        // When
        Map<String, Object> actualHealth = actuatorClient.health();

        // Then
        assertThat(actualHealth.get("status")).isEqualTo(Status.UP.getCode());
    }

    @Test
    public void is_status_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(restTemplate.getRootUri() + "/actuator/health"));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(java.time.Duration.ofMinutes(5)).hasStatus(Status.UP);
    }

    @TestConfiguration
    @EnableFeignClients
    static class DiscoveryIntTestConfiguration {
    }
}
