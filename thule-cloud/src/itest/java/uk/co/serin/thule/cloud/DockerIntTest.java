package uk.co.serin.thule.cloud;

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
    private static final String ADMIN_SERVER_URL_PREFIX = "http://docker-host:8071/";
    private static final String CONFIG_SERVICE_URL_PREFIX = "http://docker-host:8888/";
    private static final String DISCOVERY_SERVICE_URL_PREFIX = "http://docker-host:8761/";
    private static final String EDGE_SERVICE_URL_PREFIX = "http://docker-host:8080/";
    private static final String HEALTH = "health";
    private static final String INFO = "info";
    private static final String PEOPLE_SERVICE_URL_PREFIX = "http://docker-host:8090/";
    private static final String STATUS = "status";
    private static Process dockerComposeUp;
    private static RestTemplate restTemplate = new RestTemplate();
    private static RetryTemplate retryTemplate = new RetryTemplate();

    private static void assertUrlIsAvailable(String url) throws InterruptedException {
        retryTemplate.execute(context -> {
            Object response = restTemplate.getForObject(url, Object.class);
            if (response == null) {
                throw new IllegalStateException(String.format("Response from %s returned null", url));
            }
            ;
            return response;
        });
    }

    private static void dockerComposeDown() throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("docker-compose", "-f", "src/main/docker/docker-compose.yml", "down", "-v").inheritIO();
        Process dockerComposeDown = pb.start();
        dockerComposeDown.waitFor();
        if (dockerComposeUp != null) {
            dockerComposeUp.waitFor();
        }
    }

    private static void dockerComposeUp() throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("docker-compose", "-f", "src/main/docker/docker-compose.yml", "up").inheritIO();
        dockerComposeUp = pb.start();
    }

    @BeforeClass
    public static void setUpClass() throws IOException, InterruptedException {
        dockerComposeDown();
        dockerComposeUp();

        TimeoutRetryPolicy retryPolicy = new TimeoutRetryPolicy();
        retryPolicy.setTimeout(240000);

        retryTemplate.setBackOffPolicy(new ExponentialBackOffPolicy());
        retryTemplate.setRetryPolicy(retryPolicy);

        assertUrlIsAvailable(CONFIG_SERVICE_URL_PREFIX + INFO);
        assertUrlIsAvailable(DISCOVERY_SERVICE_URL_PREFIX + INFO);
        assertUrlIsAvailable(PEOPLE_SERVICE_URL_PREFIX + INFO);
        assertUrlIsAvailable(ADMIN_SERVER_URL_PREFIX + INFO);
        assertUrlIsAvailable(EDGE_SERVICE_URL_PREFIX + INFO);
    }

    @AfterClass
    public static void tearDownClass() throws InterruptedException, IOException {
        dockerComposeDown();
    }

    @Test
    public void adminServerIsUp() {
        // Given
        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        // When
        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(ADMIN_SERVER_URL_PREFIX + HEALTH, HttpMethod.GET, null, responseType);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().get(STATUS)).isEqualTo(Status.UP.getCode());
    }

    @Test
    public void configServiceIsUp() {
        // Given
        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        // When
        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(CONFIG_SERVICE_URL_PREFIX + HEALTH, HttpMethod.GET, null, responseType);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().get(STATUS)).isEqualTo(Status.UP.getCode());
    }

    @Test
    public void discoveryServiceIsUp() {
        // Given
        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        // When
        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(DISCOVERY_SERVICE_URL_PREFIX + HEALTH, HttpMethod.GET, null, responseType);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().get(STATUS)).isEqualTo(Status.UP.getCode());
    }

    @Test
    public void peopleServiceIsUp() {
        // Given
        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        // When
        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(PEOPLE_SERVICE_URL_PREFIX + HEALTH, HttpMethod.GET, null, responseType);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().get(STATUS)).isEqualTo(Status.UP.getCode());
    }

    @Test
    public void edgeServiceIsUp() {
        // Given
        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        // When
        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(EDGE_SERVICE_URL_PREFIX + HEALTH, HttpMethod.GET, null, responseType);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().get(STATUS)).isEqualTo(Status.UP.getCode());
    }
}
