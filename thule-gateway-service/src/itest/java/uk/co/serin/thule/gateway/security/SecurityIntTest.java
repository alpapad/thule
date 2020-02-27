package uk.co.serin.thule.gateway.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.co.serin.thule.test.assertj.ActuatorUri;
import uk.co.serin.thule.test.assertj.SpringBootActuatorAssert;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("itest")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityIntTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void when_accessing_the_actuator_without_authentication_then_access_should_be_granted() {
        // Given
        var actuatorUri = ActuatorUri.using(String.format("http://localhost:%s/actuator/info", port));

        // When/Then
        SpringBootActuatorAssert.assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHttpStatus(HttpStatus.OK);
    }

    @Test
    public void when_using_http_basic_authentication_then_access_should_be_denied() {
        // When
        var responseEntity = testRestTemplate.getForEntity("/hello", String.class);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
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