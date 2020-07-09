package uk.co.serin.thule.discovery;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.time.Duration;

import static org.awaitility.Awaitility.given;
import static org.awaitility.pollinterval.FixedPollInterval.fixed;
import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

class DiscoveryContractTest extends ContractBaseTest {
    @Autowired
    private DiscoveryClient discoveryClient;

    @Test
    void given_a_registered_service_when_retrieving_that_service_then_it_is_found() {
        // Given
        given().ignoreExceptions().pollInterval(fixed(Duration.ofSeconds(5))).
                await().timeout(Duration.ofMinutes(5)).
                       untilAsserted(() -> assertThat(discoveryClient.getServices()).isNotEmpty());

        // When
        var services = discoveryClient.getServices();

        // Then
        assertThat(services).contains("thule-discovery-service");
    }
}


