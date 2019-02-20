package uk.co.serin.thule.gateway.security;

import uk.co.serin.thule.test.assertj.ActuatorUri;
import uk.co.serin.thule.test.assertj.SpringBootActuatorAssert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.Duration;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

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
        ActuatorUri actuatorUri = ActuatorUri.of(URI.create(String.format("http://localhost:%s/actuator/info", port)));

        //When/Then
        SpringBootActuatorAssert.assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHttpStatus(HttpStatus.OK);
    }

    @Test
    public void when_using_http_basic_authentication_then_access_should_be_denied() {
        // Given

        //When
        ResponseEntity<String> responseEntity = testRestTemplate.getForEntity(String.format("http://localhost:%s/hello", port), String.class);

        //Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @TestConfiguration
    static class BankTransferRequestIntTestConfiguration {
        @RestController
        public class HelloWorldController {
            @RequestMapping(value = "/hello")
            public String helloWorld() {
                return "Hello";
            }
        }
    }
}