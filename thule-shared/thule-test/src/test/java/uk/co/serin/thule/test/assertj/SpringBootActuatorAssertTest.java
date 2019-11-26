package uk.co.serin.thule.test.assertj;

import org.junit.Before;
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
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class SpringBootActuatorAssertTest {
    private ActuatorUri actuatorUri = ActuatorUri.using("http://localhost");
    @Mock
    private ResponseEntity<Map<String, Object>> responseEntity;
    private ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<>() {
    };
    @Mock
    private RestTemplate restTemplate;
    private RetryTemplate retryTemplate = new RetryTemplate();
    private SpringBootActuatorAssert sut;

    @Test
    public void given_down_health_status_when_expecting_up_health_status_then_assertion_error_contains_health_status_is_down_message() {
        // Given
        given(restTemplate.exchange(actuatorUri.getUri(), HttpMethod.GET, HttpEntity.EMPTY, responseType)).willReturn(responseEntity);
        given(responseEntity.getStatusCode()).willReturn(HttpStatus.OK);
        given(responseEntity.getBody()).willReturn(Collections.singletonMap("status", "DOWN"));

        // When
        var throwable = catchThrowableOfType(() -> sut.hasHealthStatus(Status.UP), AssertionError.class);

        // Then
        assertThat(throwable).hasMessage("Expected actuator's health status to be <UP> but was <DOWN>");
    }

    @Test
    public void given_not_found_http_status_when_expecting_ok_http_status_then_assertion_error_contains_404_message() {
        // Given
        given(restTemplate.exchange(actuatorUri.getUri(), HttpMethod.GET, HttpEntity.EMPTY, responseType)).willReturn(responseEntity);
        given(responseEntity.getStatusCode()).willReturn(HttpStatus.NOT_FOUND);

        // When
        var throwable = catchThrowable(() -> sut.hasHttpStatus(HttpStatus.OK));

        // Then
        assertThat(throwable).hasMessage("Expected actuator's http status to be <200 OK> but was <404 NOT_FOUND>");
    }

    @Test
    public void given_ok_http_status_when_expecting_ok_http_status_then_same_of_sut_is_returned() {
        // Given
        given(restTemplate.exchange(actuatorUri.getUri(), HttpMethod.GET, HttpEntity.EMPTY, responseType)).willReturn(responseEntity);
        given(responseEntity.getStatusCode()).willReturn(HttpStatus.OK);

        // When
        var actualSpringBootActuatorAssert = sut.hasHttpStatus(HttpStatus.OK);

        // Then
        assertThat(actualSpringBootActuatorAssert).isSameAs(sut);
    }

    @Test
    public void given_service_unavailable_http_status_when_expecting_up_health_status_then_assertion_error_contains_404_message() {
        // Given
        retryTemplate.setRetryPolicy(new NeverRetryPolicy());
        ReflectionTestUtils.setField(sut, "retryTemplate", retryTemplate);

        given(restTemplate.exchange(actuatorUri.getUri(), HttpMethod.GET, HttpEntity.EMPTY, responseType)).willReturn(responseEntity);
        given(responseEntity.getStatusCode()).willReturn(HttpStatus.SERVICE_UNAVAILABLE);

        // When
        var throwable = catchThrowable(() -> sut.hasHealthStatus(Status.UP));

        // Then
        assertThat(throwable).hasMessageContaining("[503 SERVICE_UNAVAILABLE]");
    }

    @Test
    public void given_up_health_status_when_expecting_up_health_status_then_same_of_sut_is_returned() {
        // Given
        given(restTemplate.exchange(actuatorUri.getUri(), HttpMethod.GET, HttpEntity.EMPTY, responseType)).willReturn(responseEntity);
        given(responseEntity.getStatusCode()).willReturn(HttpStatus.OK);
        given(responseEntity.getBody()).willReturn(Collections.singletonMap("status", "UP"));

        // When
        var actualSpringBootActuatorAssert = sut.hasHealthStatus(Status.UP);

        // Then
        assertThat(actualSpringBootActuatorAssert).isSameAs(sut);
    }

    @Before
    public void setUp() {
        sut = SpringBootActuatorAssert.assertThat(actuatorUri);
        ReflectionTestUtils.setField(sut, "restTemplate", restTemplate);
    }

    @Test
    public void when_any_method_using_lombok_nonnull_annotation_in_sut_has_null_argument_then_npe_is_thrown() {
        // When/Then
        catchThrowableOfType(() -> new SpringBootActuatorAssert(null), NullPointerException.class);
        catchThrowableOfType(() -> SpringBootActuatorAssert.assertThat(null), NullPointerException.class);
        catchThrowableOfType(() -> sut.hasHealthStatus(null), NullPointerException.class);
        catchThrowableOfType(() -> sut.hasHttpStatus(null), NullPointerException.class);
        catchThrowableOfType(() -> sut.waitingForMaximum(null), NullPointerException.class);
        catchThrowableOfType(() -> sut.usingRestTemplate(null), NullPointerException.class);
        catchThrowableOfType(() -> sut.hasHealthStatus(null), NullPointerException.class);
    }

    @Test
    public void when_static_assert_that_then_an_instance_of_sut_is_returned() {
        // When
        var actualSpringBootActuatorAssert = SpringBootActuatorAssert.assertThat(actuatorUri);

        // Then
        assertThat(actualSpringBootActuatorAssert).isNotNull();
    }

    @Test
    public void when_using_a_rest_template_then_same_of_sut_is_returned() {
        // When
        var actualSpringBootActuatorAssert = sut.usingRestTemplate(new RestTemplate());

        // Then
        assertThat(actualSpringBootActuatorAssert).isSameAs(sut);
    }

    @Test
    public void when_waiting_for_maximum_of_0_seconds_then_an_illegal_argument_exception_is_thrown() {
        // When
        var throwable = catchThrowable(() -> sut.waitingForMaximum(Duration.ZERO));

        // Then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void when_waiting_for_maximum_of_1_minute_then_same_of_sut_is_returned() {
        // When
        var actualSpringBootActuatorAssert = sut.waitingForMaximum(Duration.ofMinutes(1));

        // Then
        assertThat(actualSpringBootActuatorAssert).isSameAs(sut);
    }

    @Test
    public void when_with_http_basic_then_same_of_sut_is_returned() {
        // When
        var actualSpringBootActuatorAssert = sut.withHttpBasic("username", "password");

        // Then
        assertThat(actualSpringBootActuatorAssert).isSameAs(sut);
    }
}