package uk.co.serin.thule.gateway.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.co.serin.thule.gateway.wiremock.WiremockInitializer;
import uk.co.serin.thule.test.assertj.ActuatorUri;
import uk.co.serin.thule.test.assertj.SpringBootActuatorAssert;

import java.time.Duration;

@ActiveProfiles("itest")
@ContextConfiguration(initializers = WiremockInitializer.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityIntTest {
    @LocalServerPort
    private int port;
    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void given_not_authenticated_user_when_using_service_then_access_should_be_found() {
        // When
        webTestClient.get().uri("/hello").exchange()

                     // Then
                     .expectStatus().isFound();
    }

    @Test
    public void when_accessing_the_actuator_without_authentication_then_access_should_be_granted() {
        // Given
        var actuatorUri = ActuatorUri.using(String.format("http://localhost:%s/actuator/info", port));

        // When/Then
        SpringBootActuatorAssert.assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHttpStatus(HttpStatus.OK);
    }

    @TestConfiguration
    static class SecurityIntTestConfiguration {
        @RestController
        public static class HelloWorldController {
            @GetMapping(value = "/hello")
            public String helloWorld() {
                return "Hello";
            }
        }
    }
}