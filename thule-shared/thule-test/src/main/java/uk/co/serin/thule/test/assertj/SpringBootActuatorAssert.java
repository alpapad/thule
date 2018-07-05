package uk.co.serin.thule.test.assertj;

import org.assertj.core.api.AbstractAssert;
import org.springframework.boot.actuate.health.Status;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("squid:S2160") // Suppress Subclasses that add fields should override "equals"
public class SpringBootActuatorAssert extends AbstractAssert<SpringBootActuatorAssert, ActuatorUri> {
    private static final String STATUS = "status";
    MultiValueMap<String, String> oauthHttpBodyParameters = new LinkedMultiValueMap<>();
    private HttpEntity entity = HttpEntity.EMPTY;
    private boolean isUsingOauth;
    private RestTemplate restTemplate = new RestTemplate();
    private RetryTemplate retryTemplate = new RetryTemplate();

    public SpringBootActuatorAssert(ActuatorUri actual) {
        super(actual, SpringBootActuatorAssert.class);

        retryTemplate.setRetryPolicy(new NeverRetryPolicy());
    }

    public static SpringBootActuatorAssert assertThat(ActuatorUri actual) {
        return new SpringBootActuatorAssert(actual);
    }

    public SpringBootActuatorAssert hasStatus(Status status) {
        isNotNull();
        if (isUsingOauth) {
            setUpHttpEntityForOAuth();
        }
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

    private void setUpHttpEntityForOAuth() {
        //Forms URL used to retrieve token
        String uri = actual.getUri().getScheme() + "://" + actual.getUri().getAuthority() + "/oauth/token";

        //Retrieve token
        Map response = retryTemplate.execute(context -> restTemplate.postForObject(uri, new HttpEntity<>(oauthHttpBodyParameters, null), Map.class));

        //Convert token to appropriate String format
        String token = "Bearer " + response.get("access_token").toString();

        //Sets HttpEntity, a holder, to represent both header & body of Http request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);
        entity = new HttpEntity<>("parameters", headers);
    }

    private ResponseEntity<Map<String, Object>> getResponseEntity(URI uri) {
        return retryTemplate.execute(context -> {
            ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
            };
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, responseType);
            if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                throw new IllegalStateException(String.format("Response from %s returned was not successful [%s]", uri, responseEntity.getStatusCode()));
            }
            return responseEntity;
        });
    }

    public SpringBootActuatorAssert waitingForMaximum(Duration duration) {
        Assert.isTrue(duration.getSeconds() > 0, "timeout must be positive");

        TimeoutRetryPolicy retryPolicy = new TimeoutRetryPolicy();
        retryPolicy.setTimeout(duration.getSeconds() * 1000);

        retryTemplate.setBackOffPolicy(new ExponentialBackOffPolicy());
        retryTemplate.setRetryPolicy(retryPolicy);

        return this;
    }

    public SpringBootActuatorAssert withHttpBasic(String username, String password) {
        Assert.hasLength(username, "username cannot be empty");
        Assert.hasLength(password, "password cannot be empty");

        setUpBasicAuthorizationHeader(username, password);

        return this;
    }

    private void setUpBasicAuthorizationHeader(String username, String password) {
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        interceptors = new ArrayList<>(interceptors);
        interceptors.removeIf(BasicAuthorizationInterceptor.class::isInstance);
        interceptors.add(new BasicAuthorizationInterceptor(username, password));
        restTemplate.setInterceptors(interceptors);
    }

    public SpringBootActuatorAssert withOAuth(String clientId, String clientSecret, String username, String password) {
        Assert.hasLength(clientId, "clientId cannot be empty");
        Assert.hasLength(clientSecret, "clientSecret cannot be empty");
        Assert.hasLength(username, "username cannot be empty");
        Assert.hasLength(password, "password cannot be empty");

        //Sets the body of the request
        oauthHttpBodyParameters.add("username", username);
        oauthHttpBodyParameters.add("password", password);
        oauthHttpBodyParameters.add("grant_type", "password");

        //Set the header of the Http Request
        setUpBasicAuthorizationHeader(clientId, clientSecret);

        //Sets boolean to become true, indicating to hasStatus  using OAuth
        isUsingOauth = true;

        return this;
    }
}