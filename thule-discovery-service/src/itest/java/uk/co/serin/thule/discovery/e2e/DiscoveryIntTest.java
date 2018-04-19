package uk.co.serin.thule.discovery.e2e;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import uk.co.serin.thule.test.assertj.ActuatorUri;

import java.net.URI;
import java.util.Map;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles({"itest", "${spring.profiles.include:default}"})
@RunWith(SpringRunner.class)
public class DiscoveryIntTest {
    @Autowired
    private EchoServiceClient echoServiceClient;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void can_discover_a_service() throws InterruptedException {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(restTemplate.getRootUri() + "/actuator/health"));
        assertThat(actuatorUri).waitingForMaximum(java.time.Duration.ofMinutes(5)).hasStatus(Status.UP);

        String expectedMessage = "Hello world";

        // When
        ResponseEntity<Map<String, String>> echoResponseEntity
                = restTemplate.exchange("/echo/{message}", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<Map<String, String>>() {
        }, expectedMessage);

        // Then
        String actualMessage = echoResponseEntity.getBody().get("echo");
        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
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
