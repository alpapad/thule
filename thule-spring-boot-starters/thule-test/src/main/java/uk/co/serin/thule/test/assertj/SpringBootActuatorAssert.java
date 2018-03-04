package uk.co.serin.thule.test.assertj;

import org.assertj.core.api.AbstractAssert;
import org.springframework.boot.actuate.health.Status;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("squid:S2160") // Suppress Subclasses that add fields should override "equals"
public class SpringBootActuatorAssert extends AbstractAssert<SpringBootActuatorAssert, ActuatorUri> {
    private static final String STATUS = "status";
    private RestTemplate restTemplate = new RestTemplate();
    private RetryTemplate retryTemplate = new RetryTemplate();

    public SpringBootActuatorAssert(ActuatorUri actual) {
        super(actual, SpringBootActuatorAssert.class);
        TimeoutRetryPolicy retryPolicy = new TimeoutRetryPolicy();
        retryPolicy.setTimeout(600000);

        retryTemplate.setBackOffPolicy(new ExponentialBackOffPolicy());
        retryTemplate.setRetryPolicy(retryPolicy);
    }

    public static SpringBootActuatorAssert assertThat(ActuatorUri actual) {
        return new SpringBootActuatorAssert(actual);
    }

    public SpringBootActuatorAssert hasStatus(Status status) {
        isNotNull();

        try {
            ResponseEntity<Map<String, Object>> responseEntity = getResponseEntity(actual.getUri());
            if (!responseEntity.getBody().get(STATUS).equals(status.getCode())) {
                throw new AssertionError(String.format("Expected actuator's status to be <%s> but was <%s>", status.getCode(), responseEntity.getBody().get(STATUS)));
            }
        } catch (Exception exception) {
            throw new AssertionError(String.format("Failed to obtain status from ActuatorUri %s %n%s", actual, exception));
        }

        return this;
    }

    private ResponseEntity<Map<String, Object>> getResponseEntity(URI uri) {
        return retryTemplate.execute(context -> {
            ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
            };
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, HttpEntity.EMPTY, responseType);
            if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                throw new IllegalStateException(String.format("Response from %s returned was not successful [%s]", uri, responseEntity.getStatusCode()));
            }

            return responseEntity;
        });
    }

    public SpringBootActuatorAssert withCredentials(String username, String password) {
        Assert.hasLength(username, "username cannot be empty");
        Assert.hasLength(password, "password cannot be empty");

        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        interceptors = new ArrayList<>(interceptors);
        interceptors.removeIf(BasicAuthorizationInterceptor.class::isInstance);
        interceptors.add(new BasicAuthorizationInterceptor(username, password));
        restTemplate.setInterceptors(interceptors);

        return this;
    }
}