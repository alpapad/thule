package uk.co.serin.thule.config.docker;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.actuate.health.Status;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class DockerIntTest {
    private static final String APPLICATION_STATUS = "/application/status";
    private static final String CONFIG_SERVICE_URL_PREFIX = "http://docker-host:8888";
    private static final String STATUS = "status";
    private static Process dockerComposeUp;
    private static RestTemplate restTemplate = new RestTemplate();
    private static RetryTemplate retryTemplate = new RetryTemplate();

    @BeforeClass
    public static void setUpClass() throws IOException {
        dockerComposeDown();
        dockerComposeUp();

        TimeoutRetryPolicy retryPolicy = new TimeoutRetryPolicy();
        retryPolicy.setTimeout(600000);

        retryTemplate.setBackOffPolicy(new ExponentialBackOffPolicy());
        retryTemplate.setRetryPolicy(retryPolicy);
    }

    private static void dockerComposeDown() throws IOException {
        ProcessBuilder pb = new ProcessBuilder("docker-compose", "-f", "src/itest/docker/docker-compose.yml", "down", "-v").inheritIO();
        Process dockerComposeDown = pb.start();
        try {
            dockerComposeDown.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (dockerComposeUp != null) {
            try {
                dockerComposeUp.waitFor();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void dockerComposeUp() throws IOException {
        ProcessBuilder pb = new ProcessBuilder("docker-compose", "-f", "src/itest/docker/docker-compose.yml", "up").inheritIO();
        dockerComposeUp = pb.start();
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        dockerComposeDown();
    }

    @Test
    public void config_service_is_up() {
        // Given

        // When
        ResponseEntity<Map<String, Object>> responseEntity = getResponseEntity(CONFIG_SERVICE_URL_PREFIX + APPLICATION_STATUS);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().get(STATUS)).isEqualTo(Status.UP.getCode());
    }

    private static ResponseEntity<Map<String, Object>> getResponseEntity(String url) {
        return getResponseEntity(url, restTemplate);
    }

    private static ResponseEntity<Map<String, Object>> getResponseEntity(String url, RestTemplate restTemplate) {
        return retryTemplate.execute(context -> {
            ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
            };
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            if (responseEntity == null) {
                throw new IllegalStateException(String.format("Response from %s returned null", url));
            }

            return responseEntity;
        });
    }
}
