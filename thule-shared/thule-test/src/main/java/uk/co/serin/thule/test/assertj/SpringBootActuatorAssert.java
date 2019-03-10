package uk.co.serin.thule.test.assertj;

import org.assertj.core.api.AbstractAssert;
import org.springframework.boot.actuate.health.Status;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Map;

import lombok.NonNull;

@SuppressWarnings("squid:S2160") // Suppress Subclasses that add fields should override "equals"
public class SpringBootActuatorAssert extends AbstractAssert<SpringBootActuatorAssert, ActuatorUri> {
    private static final String STATUS = "status";
    private RestTemplate restTemplate = new RestTemplate();
    private RetryTemplate retryTemplate = new RetryTemplate();

    public SpringBootActuatorAssert(@NonNull ActuatorUri actual) {
        super(actual, SpringBootActuatorAssert.class);

        retryTemplate.setRetryPolicy(new NeverRetryPolicy());
    }

    public static SpringBootActuatorAssert assertThat(@NonNull ActuatorUri actual) {
        return new SpringBootActuatorAssert(actual);
    }

    public SpringBootActuatorAssert hasHealthStatus(@NonNull Status status) {
        try {
            var responseEntity = getResponseEntity(actual.getUri());
            if (!responseEntity.getBody().get(STATUS).equals(status.getCode())) {
                throw new AssertionError(
                        String.format("Expected actuator's health status to be <%s> but was <%s>", status.getCode(), responseEntity.getBody().get(STATUS)));
            }
        } catch (Exception exception) {
            throw new AssertionError(String.format("Failed to obtain health status from ActuatorUri %s %n%s", actual, exception));
        }
        return this;
    }

    private ResponseEntity<Map<String, Object>> getResponseEntity(URI uri) {
        return retryTemplate.execute(context -> {
            var responseType = new ParameterizedTypeReference<Map<String, Object>>() {
            };
            var responseEntity = restTemplate.exchange(uri, HttpMethod.GET, HttpEntity.EMPTY, responseType);
            if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                throw new IllegalStateException(String.format("Response from %s returned was not successful [%s]", uri, responseEntity.getStatusCode()));
            }
            return responseEntity;
        });
    }

    public SpringBootActuatorAssert hasHttpStatus(@NonNull HttpStatus httpStatus) {
        var responseEntity = getHttpResponseEntity(actual.getUri());
        if (!responseEntity.getStatusCode().equals(httpStatus)) {
            throw new AssertionError(String.format("Expected actuator's http status to be <%s> but was <%s>", httpStatus, responseEntity.getStatusCode()));
        }
        return this;
    }

    private ResponseEntity<Map<String, Object>> getHttpResponseEntity(URI uri) {
        return retryTemplate.execute(context -> {
            var responseType = new ParameterizedTypeReference<Map<String, Object>>() {
            };
            return restTemplate.exchange(uri, HttpMethod.GET, HttpEntity.EMPTY, responseType);
        });
    }

    public SpringBootActuatorAssert usingRestTemplate(@NonNull RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

        return this;
    }

    public SpringBootActuatorAssert waitingForMaximum(@NonNull Duration duration) {
        Assert.isTrue(duration.getSeconds() > 0, "timeout must be positive");

        var retryPolicy = new TimeoutRetryPolicy();
        retryPolicy.setTimeout(duration.getSeconds() * 1000);

        retryTemplate.setBackOffPolicy(new ExponentialBackOffPolicy());
        retryTemplate.setRetryPolicy(retryPolicy);

        return this;
    }

    public SpringBootActuatorAssert withHttpBasic(String username, String password) {
        Assert.hasLength(username, "username cannot be empty");
        Assert.hasLength(password, "password cannot be empty");

        // Add basic authentication credentials
        var interceptors = restTemplate.getInterceptors();
        interceptors = new ArrayList<>(interceptors);
        interceptors.removeIf(BasicAuthenticationInterceptor.class::isInstance);
        interceptors.add(new BasicAuthenticationInterceptor(username, password));
        restTemplate.setInterceptors(interceptors);

        return this;
    }
}