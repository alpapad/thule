package uk.co.serin.thule.discovery;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.test.context.junit4.SpringRunner;

import uk.co.serin.thule.utils.docker.DockerCompose;

import java.io.IOException;
import java.time.Duration;

import static org.awaitility.Awaitility.given;
import static org.awaitility.pollinterval.FixedPollInterval.fixed;
import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

/**
 * Tests for the Discovery service.
 *
 * The objective of these tests is to prove that a service registered within the Discovery Service can be retrieved and executed successfully:
 *
 * <ul>
 * <li>Start the discovery service</li>
 * <li>Register a target service in the Discovery Service</li>
 * Rather than create another service to act as the target to test the Discovery Service, we will use the Discovery Service itself as the target to test the Discovery Service.
 * To that end the Discovery Service has been configured for these tests to register itself at startup.
 * <li>Retrieve a target service from the Discovery Service</li>
 * In other words, retrieve the Discovery Service to act as the target from the Discovery Service
 * <li>Invoke an endpoint on the target service</li>
 * Use the actuator on the target Discovery Service
 * </ul>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DiscoveryDockerTest {
    private static final DockerCompose DOCKER_COMPOSE = new DockerCompose("src/dtest/docker/docker-compose.yml");
    @Autowired
    private ActuatorClient actuatorClient;
    @Autowired
    private DiscoveryClient discoveryClient;

    @BeforeClass
    public static void setUpClass() throws IOException {
        DOCKER_COMPOSE.downAndUp();
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        DOCKER_COMPOSE.down();
    }

    @Test
    public void when_checking_health_of_a_service_via_the_discovery_service_then_its_status_is_up() {
        // Given
        given().ignoreExceptions().pollInterval(fixed(Duration.ofSeconds(5))).
                await().timeout(Duration.ofMinutes(5)).
                       untilAsserted(() -> assertThat(discoveryClient.getServices()).contains("thule-discovery-service"));

        // When
        var actualHealth = actuatorClient.health();

        // Then
        assertThat(actualHealth.get("status")).isEqualTo(Status.UP.getCode());
    }
}