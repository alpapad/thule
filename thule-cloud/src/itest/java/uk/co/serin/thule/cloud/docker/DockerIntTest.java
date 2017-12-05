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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import uk.co.serin.thule.test.assertj.ActuatorUri;
import uk.co.serin.thule.utils.utils.DockerCompose;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

public class DockerIntTest {
    private static final String ADMIN_SERVER_URL_PREFIX = "http://docker-host:9071";
    private static final String ACTUATOR_HEALTH = "/actuator/health";
    private static final String CONFIG_SERVICE_URL_PREFIX = "http://docker-host:9888";
    private static final String DISCOVERY_SERVICE_URL_PREFIX = "http://docker-host:9761";
    private static final String EDGE_SERVER_URL_PREFIX = "http://docker-host:9080";
    private static final String EMAIL_SERVICE_URL_PREFIX = "http://docker-host:9091";
    private static final String HEALTH = "/health";
    private static final String PEOPLE = "/people";
    private static final String PEOPLE_SERVICE_URL_PREFIX = "http://docker-host:9090";
    private static final String THULE_EMAIL_SERVICE = "/thule-email-service";
    private static final String THULE_PEOPLE_SERVICE = "/thule-people-service";
    private static DockerCompose dockerComposeUtils = new DockerCompose("src/itest/docker/docker-compose.yml");
    private static RetryTemplate retryTemplate = new RetryTemplate();

    @BeforeClass
    public static void setUpClass() throws IOException {
        dockerComposeUtils.downAndUp();
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        dockerComposeUtils.down();
    }

    @Test
    public void admin_server_is_up() {
        assertThat(new ActuatorUri(URI.create(ADMIN_SERVER_URL_PREFIX + HEALTH))).hasStatus(Status.UP);
    }

    @Test
    public void config_service_is_up() {
        assertThat(new ActuatorUri(URI.create(CONFIG_SERVICE_URL_PREFIX + ACTUATOR_HEALTH))).hasStatus(Status.UP);
    }

    @Test
    public void discovery_service_is_up() {
        assertThat(new ActuatorUri(URI.create(DISCOVERY_SERVICE_URL_PREFIX + ACTUATOR_HEALTH))).hasStatus(Status.UP);
    }

    @Test
    public void edge_server_is_up() {
        assertThat(new ActuatorUri(URI.create(EDGE_SERVER_URL_PREFIX + ACTUATOR_HEALTH))).hasStatus(Status.UP);
    }

    @Test
    public void edge_server_proxies_email_service() {
        assertThat(new ActuatorUri(URI.create(EDGE_SERVER_URL_PREFIX + THULE_EMAIL_SERVICE + ACTUATOR_HEALTH))).hasStatus(Status.UP);
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

    private static ResponseEntity<Map<String, Object>> getResponseEntity(String url, RestTemplate restTemplate) {
        return retryTemplate.execute(context -> {
            ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
            };
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, responseType);
            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                throw new IllegalStateException(String.format("Response from %s returned was not successful [%s]", url, responseEntity.getStatusCodeValue()));
            }

            return responseEntity;
        });
    }

    @Test
    public void email_service_is_up() {
        assertThat(new ActuatorUri(URI.create(EMAIL_SERVICE_URL_PREFIX + ACTUATOR_HEALTH))).hasStatus(Status.UP);
    }

    @Test
    public void people_service_is_up() {
        assertThat(new ActuatorUri(URI.create(PEOPLE_SERVICE_URL_PREFIX + HEALTH))).hasStatus(Status.UP);
    }
}
