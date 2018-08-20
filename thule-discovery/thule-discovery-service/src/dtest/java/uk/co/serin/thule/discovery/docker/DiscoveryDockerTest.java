package uk.co.serin.thule.discovery.docker;

import com.gohenry.utils.docker.DockerCompose;

import org.awaitility.Duration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;

import static com.gohenry.test.assertj.GoHenryAssertions.assertThat;
import static org.awaitility.Awaitility.given;
import static org.awaitility.pollinterval.FixedPollInterval.fixed;

/**
 * Tests for the Discovery service.
 *
 * The objective of these tests is to prove that a service registered within the Discovery Service can be retrieved and executed successfully:
 *
 * <ul>
 *      <li>Start the discovery service</li>
 *      <li>Register a target service in the Discovery Service</li>
 *      Rather than create another service to act as the target to test the Discovery Service, we will use the Discovery Service itself as the target to test the Discovery Service.
 *      To that end the Discovery Service has been configured for these tests to register itself at startup.
 *      <li>Retrieve a target service from the Discovery Service</li>
 *      In other words, retrieve the Discovery Service to act as the target from the Discovery Service
 *      <li>Invoke an endpoint on the target service</li>
 *      Use the actuator on the target Discovery Service
 * </ul>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DiscoveryDockerTest {
    private static DockerCompose dockerCompose = new DockerCompose("src/dtest/docker/thule-discovery-docker-tests/docker-compose.yml");
    @Autowired
    private ActuatorClient actuatorClient;
    @Autowired
    private DiscoveryClient discoveryClient;

    @BeforeClass
    public static void setUpClass() throws IOException {
        dockerCompose.downAndUp();
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        dockerCompose.down();
    }

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

    @TestConfiguration
    @EnableFeignClients
    static class ContainerTestConfiguration {
    }
}