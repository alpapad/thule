package uk.co.serin.thule.discovery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.util.SocketUtils;

import uk.co.serin.thule.test.assertj.ActuatorUri;

import java.time.Duration;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

/**
 * Tests for the Discovery service.
 *
 * It is good practise for the tests to use a random unused port to ensure that their are no clashes with existing ports in use. If we use
 * SpringBootTest.WebEnvironment.RANDOM_PORT, this port will be used by the server as intended. However, the Eureka client that is required as part of
 * the server will be unaware of the random port because it is initialized before the random port is determined. To overcome this, we use a
 * ApplicationContextInitializer which sets the SpringBootTest.WebEnvironment.DEFINED_PORT, i.e. server.port, before the application is initialized.
 * This way both the application server and the eureka client can use a 'random' port.
 */
@ActiveProfiles("ctest")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(initializers = HealthCheckContractTest.RandomPortInitializer.class)
public class HealthCheckContractTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void when_checking_health_then_status_is_up() {
        // Given
        var actuatorUri = ActuatorUri.of(restTemplate.getRootUri() + "/actuator/health");

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }

    static class RandomPortInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            var randomPort = SocketUtils.findAvailableTcpPort();
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext, "server.port=" + randomPort);
        }
    }
}
