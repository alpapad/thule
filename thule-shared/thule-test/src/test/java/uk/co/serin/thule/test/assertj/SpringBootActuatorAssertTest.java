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
        ActuatorUri actuatorUri = ActuatorUri.of("http://localhost");

        // When
        SpringBootActuatorAssert sut = SpringBootActuatorAssert.assertThat(actuatorUri);

        // Then
        assertThat(sut).isNotNull();
    }

    @Test
    public void hasHealthStatus_fails_when_status_is_not_as_expected() {
        // Given
        ActuatorUri actuatorUri = ActuatorUri.of("http://localhost");

        SpringBootActuatorAssert sut = SpringBootActuatorAssert.assertThat(actuatorUri);
        ReflectionTestUtils.setField(sut, "restTemplate", restTemplate);

        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        given(restTemplate.exchange(actuatorUri.getUri(), HttpMethod.GET, HttpEntity.EMPTY, responseType)).willReturn(responseEntity);
        given(responseEntity.getStatusCode()).willReturn(HttpStatus.OK);
        given(responseEntity.getBody()).willReturn(Collections.singletonMap("status", "DOWN"));

        // When
        try {
            sut.hasHealthStatus(Status.UP);
            fail();
        } catch (AssertionError error) {
            // Then
            assertThat(error).hasMessage("Expected actuator's health status to be <UP> but was <DOWN>");
        }
    }

    @Test
    public void hasHttpStatus_service_is_unavailable_with_http_status_404() {
        // Given
        ActuatorUri actuatorUri = ActuatorUri.of("http://localhost");

        SpringBootActuatorAssert sut = SpringBootActuatorAssert.assertThat(actuatorUri);
        ReflectionTestUtils.setField(sut, "restTemplate", restTemplate);

        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        given(restTemplate.exchange(actuatorUri.getUri(), HttpMethod.GET, HttpEntity.EMPTY, responseType)).willReturn(responseEntity);
        given(responseEntity.getStatusCode()).willReturn(HttpStatus.NOT_FOUND);

        // When
        try {
            sut.hasHttpStatus(HttpStatus.OK);
            fail();
        } catch (AssertionError error) {
            // Then
            assertThat(error).hasMessageContaining("404");
        }
    }

    @Test
    public void hasHttpStatus_succeeds() {
        // Given
        ActuatorUri actuatorUri = ActuatorUri.of("http://localhost");

        SpringBootActuatorAssert sut = SpringBootActuatorAssert.assertThat(actuatorUri);
        ReflectionTestUtils.setField(sut, "restTemplate", restTemplate);

        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        given(restTemplate.exchange(actuatorUri.getUri(), HttpMethod.GET, HttpEntity.EMPTY, responseType)).willReturn(responseEntity);
        given(responseEntity.getStatusCode()).willReturn(HttpStatus.OK);

        // When
        sut = sut.hasHttpStatus(HttpStatus.OK);

        // Then
        assertThat(sut).isNotNull();
    }

    @Test
    public void hasStatus_fails_when_service_is_unavailable() {
        // Given
        ActuatorUri actuatorUri = ActuatorUri.of("http://localhost");

        retryTemplate.setRetryPolicy(new NeverRetryPolicy());

        SpringBootActuatorAssert sut = SpringBootActuatorAssert.assertThat(actuatorUri);
        ReflectionTestUtils.setField(sut, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(sut, "retryTemplate", retryTemplate);

        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        given(restTemplate.exchange(actuatorUri.getUri(), HttpMethod.GET, HttpEntity.EMPTY, responseType)).willReturn(responseEntity);
        given(responseEntity.getStatusCode()).willReturn(HttpStatus.SERVICE_UNAVAILABLE);

        // When
        try {
            sut.hasHealthStatus(Status.UP);
            fail();
        } catch (AssertionError error) {
            // Then
            assertThat(error).hasMessageContaining("[503 SERVICE_UNAVAILABLE]");
        }
    }

    @Test
    public void hasStatus_succeeds_when_status_is_as_expected() {
        // Given
        ActuatorUri actuatorUri = ActuatorUri.of("http://localhost");

        SpringBootActuatorAssert sut = SpringBootActuatorAssert.assertThat(actuatorUri);
        ReflectionTestUtils.setField(sut, "restTemplate", restTemplate);

        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        given(restTemplate.exchange(actuatorUri.getUri(), HttpMethod.GET, HttpEntity.EMPTY, responseType)).willReturn(responseEntity);
        given(responseEntity.getStatusCode()).willReturn(HttpStatus.OK);
        given(responseEntity.getBody()).willReturn(Collections.singletonMap("status", "UP"));

        // When
        SpringBootActuatorAssert actualSpringBootActuatorAssert = sut.hasHealthStatus(Status.UP);

        // Then
        assertThat(actualSpringBootActuatorAssert).isNotNull();
    }

    @Test
    public void uaingRestTemplate_succeeds() {
        // Given
        ActuatorUri actuatorUri = ActuatorUri.of("http://localhost");
        SpringBootActuatorAssert sut = SpringBootActuatorAssert.assertThat(actuatorUri);

        // When
        SpringBootActuatorAssert actualSpringBootActuatorAssert = sut.usingRestTemplate(new RestTemplate());

        // Then
        assertThat(actualSpringBootActuatorAssert).isNotNull();
    }

    @Test
    public void waitingForMaximum_of_1_minute_succeeds() {
        // Given
        ActuatorUri actuatorUri = ActuatorUri.of("http://localhost");
        SpringBootActuatorAssert sut = SpringBootActuatorAssert.assertThat(actuatorUri);

        // When
        SpringBootActuatorAssert actualSpringBootActuatorAssert = sut.waitingForMaximum(Duration.ofMinutes(1));

        // Then
        assertThat(actualSpringBootActuatorAssert).isNotNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void waitingForMaximum_of_zero_minutes_throws_exception() {
        // Given
        ActuatorUri actuatorUri = ActuatorUri.of("http://localhost");
        SpringBootActuatorAssert sut = SpringBootActuatorAssert.assertThat(actuatorUri);

        // When
        sut.waitingForMaximum(Duration.ZERO);

        // Then (see expected in @Test annotation)
    }

    @Test
    public void withHttpBasic_succeeds() {
        // Given
        ActuatorUri actuatorUri = ActuatorUri.of("http://localhost");
        SpringBootActuatorAssert sut = SpringBootActuatorAssert.assertThat(actuatorUri);

        // When
        SpringBootActuatorAssert actualSpringBootActuatorAssert = sut.withHttpBasic("username", "password");

        // Then
        assertThat(actualSpringBootActuatorAssert).isNotNull();
    }
}