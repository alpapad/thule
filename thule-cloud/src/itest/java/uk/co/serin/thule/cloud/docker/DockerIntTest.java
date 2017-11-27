package uk.co.serin.thule.cloud.docker;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.actuate.health.Status;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class DockerIntTest {
    private static final String ADMIN_SERVER_URL_PREFIX = "http://docker-host:8071";
    private static final String APPLICATION_STATUS = "/application/status";
    private static final String CONFIG_SERVICE_URL_PREFIX = "http://docker-host:8888";
    private static final String DISCOVERY_SERVICE_URL_PREFIX = "http://docker-host:8761";
    private static final String EDGE_SERVER_URL_PREFIX = "http://docker-host:8080";
    private static final String EMAIL_SERVICE_URL_PREFIX = "http://docker-host:8091";
    private static final String HEALTH = "/health";
    private static final String PEOPLE = "/people";
    private static final String PEOPLE_SERVICE_URL_PREFIX = "http://docker-host:8090";
    private static final String STATUS = "status";
    private static final String THULE_EMAIL_SERVICE = "/thule-email-service";
    private static final String THULE_PEOPLE_SERVICE = "/thule-people-service";
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
    public void admin_server_is_up() {
        // Given

        // When
        ResponseEntity<Map<String, Object>> responseEntity = getResponseEntity(ADMIN_SERVER_URL_PREFIX + HEALTH);

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

    @Test
    public void config_service_is_up() {
        // Given

        // When
        ResponseEntity<Map<String, Object>> responseEntity = getResponseEntity(CONFIG_SERVICE_URL_PREFIX + APPLICATION_STATUS);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().get(STATUS)).isEqualTo(Status.UP.getCode());
    }

    @Test
    public void discovery_service_is_up() {
        // Given

        // When
        ResponseEntity<Map<String, Object>> responseEntity = getResponseEntity(DISCOVERY_SERVICE_URL_PREFIX + APPLICATION_STATUS);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().get(STATUS)).isEqualTo(Status.UP.getCode());
    }

    @Test
    public void edge_server_is_up() {
        // Given

        // When
        ResponseEntity<Map<String, Object>> responseEntity = getResponseEntity(EDGE_SERVER_URL_PREFIX + APPLICATION_STATUS);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().get(STATUS)).isEqualTo(Status.UP.getCode());
    }

    @Test
    public void edge_server_proxies_email_service() {
        // Given

        // When
        ResponseEntity<Map<String, Object>> responseEntity = getResponseEntity(EDGE_SERVER_URL_PREFIX + THULE_EMAIL_SERVICE + APPLICATION_STATUS);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().get(STATUS)).isEqualTo(Status.UP.getCode());
    }

    @Test
    public void edge_server_proxies_people_service() {
        // Given
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("admin", "admin"));
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

        RestTemplate restTemplateWithCredentials = new RestTemplate(requestFactory);

        // When
        ResponseEntity<Map<String, Object>> responseEntity = getResponseEntity(EDGE_SERVER_URL_PREFIX + THULE_PEOPLE_SERVICE + PEOPLE, restTemplateWithCredentials);

        // Then
        Map embedded = Map.class.cast(responseEntity.getBody().get("_embedded"));
        List people = List.class.cast(embedded.get("people"));

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(people).hasSize(8);
    }

    @Test
    public void email_service_is_up() {
        // Given

        // When
        ResponseEntity<Map<String, Object>> responseEntity = getResponseEntity(EMAIL_SERVICE_URL_PREFIX + APPLICATION_STATUS);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().get(STATUS)).isEqualTo(Status.UP.getCode());
    }

    @Test
    public void people_service_is_up() {
        // Given

        // When
        ResponseEntity<Map<String, Object>> responseEntity = getResponseEntity(PEOPLE_SERVICE_URL_PREFIX + HEALTH);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().get(STATUS)).isEqualTo(Status.UP.getCode());
    }
}
