package uk.co.serin.thule.discovery;

import org.awaitility.Duration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.util.SocketUtils;

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
@ActiveProfiles("ctest")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = HealthCheckContractTest.RandomPortInitailizer.class)
public class HealthCheckContractTest {
    @Autowired
    private ActuatorClient actuatorClient;
    @Autowired
    private DiscoveryClient discoveryClient;
    @LocalServerPort
    private int port;

    @Test
    public void when_checking_health_of_a_service_via_the_discovery_service_then_its_status_is_up() {
        // Given
        given().ignoreExceptions().pollInterval(fixed(Duration.FIVE_SECONDS)).
                await().timeout(Duration.FIVE_MINUTES).
                       untilAsserted(() -> {
                           assertThat(discoveryClient.getServices()).contains("thule-discovery-service");
                       });

        // When
        var actualHealth = actuatorClient.health();

        // Then
        assertThat(actualHealth.get("status")).isEqualTo(Status.UP.getCode());
    }

    @TestConfiguration
    @EnableFeignClients
    static class DiscoveryIntTestConfiguration {
    }

    public static class RandomPortInitailizer implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {
        /**
         * Get the order value of this object.
         * <p>Higher values are interpreted as lower priority. As a consequence,
         * the object with the lowest value has the highest priority (somewhat
         * analogous to Servlet {@code load-on-startup} values).
         * <p>Same order values will result in arbitrary sort positions for the
         * affected objects.
         *
         * @return the order value
         * @see #HIGHEST_PRECEDENCE
         * @see #LOWEST_PRECEDENCE
         */
        @Override
        public int getOrder() {
            return Ordered.LOWEST_PRECEDENCE;
        }

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            int randomPort = SocketUtils.findAvailableTcpPort();
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext,
                    "server.port=" + randomPort);
        }

    }
}
