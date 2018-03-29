package uk.co.serin.thule.test.assertj;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.actuate.health.Status;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class SpringBootActuatorAssertTest {
    @Mock
    private ResponseEntity<Map<String, Object>> responseEntity;
    @Mock
    private RestTemplate restTemplate;
    private RetryTemplate retryTemplate = new RetryTemplate();

    @Test
    public void assertThat_instantiates_assertion() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create("http://localhost"));

        // When
        SpringBootActuatorAssert actualSpringBootActuatorAssert = SpringBootActuatorAssert.assertThat(actuatorUri);

        // Then
        assertThat(actualSpringBootActuatorAssert).isNotNull();
    }

    @Test
    public void hasStatus_fails_when_service_is_unavailable() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create("http://localhost"));

        retryTemplate.setRetryPolicy(new NeverRetryPolicy());

        SpringBootActuatorAssert springBootActuatorAssert = SpringBootActuatorAssert.assertThat(actuatorUri);
        ReflectionTestUtils.setField(springBootActuatorAssert, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(springBootActuatorAssert, "retryTemplate", retryTemplate);

        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        given(restTemplate.exchange(actuatorUri.getUri(), HttpMethod.GET, HttpEntity.EMPTY, responseType)).willReturn(responseEntity);
        given(responseEntity.getStatusCode()).willReturn(HttpStatus.SERVICE_UNAVAILABLE);

        // When
        try {
            springBootActuatorAssert.hasStatus(Status.UP);
            fail();
        } catch (AssertionError error) {
            // Then
            assertThat(error).hasMessageContaining("not successful [503]");
        }
    }

    @Test
    public void hasStatus_fails_when_status_is_not_as_expected() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create("http://localhost"));

        SpringBootActuatorAssert springBootActuatorAssert = SpringBootActuatorAssert.assertThat(actuatorUri);
        ReflectionTestUtils.setField(springBootActuatorAssert, "restTemplate", restTemplate);

        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        given(restTemplate.exchange(actuatorUri.getUri(), HttpMethod.GET, HttpEntity.EMPTY, responseType)).willReturn(responseEntity);
        given(responseEntity.getStatusCode()).willReturn(HttpStatus.OK);
        given(responseEntity.getBody()).willReturn(Collections.singletonMap("status", "DOWN"));

        // When
        try {
            springBootActuatorAssert.hasStatus(Status.UP);
            fail();
        } catch (AssertionError error) {
            // Then
            assertThat(error).hasMessage("Expected actuator's status to be <UP> but was <DOWN>");
        }
    }

    @Test
    public void hasStatus_succeeds_when_status_is_as_expected() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create("http://localhost"));

        SpringBootActuatorAssert springBootActuatorAssert = SpringBootActuatorAssert.assertThat(actuatorUri);
        ReflectionTestUtils.setField(springBootActuatorAssert, "restTemplate", restTemplate);

        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        given(restTemplate.exchange(actuatorUri.getUri(), HttpMethod.GET, HttpEntity.EMPTY, responseType)).willReturn(responseEntity);
        given(responseEntity.getStatusCode()).willReturn(HttpStatus.OK);
        given(responseEntity.getBody()).willReturn(Collections.singletonMap("status", "UP"));

        // When
        SpringBootActuatorAssert actualSpringBootActuatorAssert = springBootActuatorAssert.hasStatus(Status.UP);

        // Then
        assertThat(actualSpringBootActuatorAssert).isNotNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void waitingForMaximum_of_zero_minutes_throws_exception() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create("http://localhost"));

        SpringBootActuatorAssert springBootActuatorAssert = SpringBootActuatorAssert.assertThat(actuatorUri);
        ReflectionTestUtils.setField(springBootActuatorAssert, "restTemplate", restTemplate);

        // When
        springBootActuatorAssert.waitingForMaximum(Duration.ZERO);

        // Then (see expected in @Test annotation)
    }


    @Test
    public void waitingForMaximum_of_1_minute_applies() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create("http://localhost"));

        SpringBootActuatorAssert springBootActuatorAssert = SpringBootActuatorAssert.assertThat(actuatorUri);
        ReflectionTestUtils.setField(springBootActuatorAssert, "restTemplate", restTemplate);

        // When
        SpringBootActuatorAssert actualSpringBootActuatorAssert = springBootActuatorAssert.waitingForMaximum(Duration.ofMinutes(1));

        // Then
        assertThat(actualSpringBootActuatorAssert).isNotNull();
    }

    @Test
    public void withCredentials() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create("http://localhost"));

        SpringBootActuatorAssert springBootActuatorAssert = SpringBootActuatorAssert.assertThat(actuatorUri);
        ReflectionTestUtils.setField(springBootActuatorAssert, "restTemplate", restTemplate);

        // When
        SpringBootActuatorAssert actualSpringBootActuatorAssert = springBootActuatorAssert.withCredentials("username", "password");

        // Then
        assertThat(actualSpringBootActuatorAssert).isNotNull();
    }
}