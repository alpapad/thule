package uk.co.serin.thule.discovery.container;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import uk.co.serin.thule.utils.docker.DockerCompose;

import java.io.IOException;
import java.util.Map;

import static org.awaitility.Awaitility.given;
import static org.awaitility.pollinterval.FixedPollInterval.fixed;
import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@ActiveProfiles({"ctest", "${spring.profiles.include:default}"})
@SpringBootTest
@RunWith(SpringRunner.class)
public class ContainerTest {
    private static DockerCompose dockerCompose = new DockerCompose("src/ctest/docker/thule-discovery-service-container-tests/docker-compose.yml");
    @Autowired
    private ActuatorClient actuatorClient;
    @Autowired
    private Environment env;

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
        given().ignoreExceptions().pollInterval(fixed(org.awaitility.Duration.FIVE_SECONDS)).
                await().timeout(org.awaitility.Duration.FIVE_MINUTES).
                untilAsserted(() -> {
                    actuatorClient.health();
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