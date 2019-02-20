package uk.co.serin.thule.configuration;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import uk.co.serin.thule.test.assertj.ActuatorUri;
import uk.co.serin.thule.utils.docker.DockerCompose;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigurationDockerTest {
    private static DockerCompose dockerCompose = new DockerCompose("src/dtest/docker/thule-configuration-docker-tests/docker-compose.yml");
    @Value("${thule.configurationservice.api.host}")
    private String configurationServiceApiHost;
    @Value("${thule.configurationservice.api.port}")
    private int configurationServiceApiPort;
    private String configurationServiceBaseUrl;
    private RestTemplate restTemplate = new RestTemplate();

    @BeforeClass
    public static void setUpClass() throws IOException {
        dockerCompose.downAndUp();
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        dockerCompose.down();
    }

    @Before
    public void setUp() {
        configurationServiceBaseUrl = String.format("http://%s:%s", configurationServiceApiHost, configurationServiceApiPort);
    }

    @Test
    public void when_healthy_then_configuration_for_the_discovery_service_exists() {
        // Given
        when_checking_health_then_status_is_up();
        var responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        // When
        var responseEntity =
                restTemplate.exchange(configurationServiceBaseUrl + "thule-discovery-service/default", HttpMethod.GET, HttpEntity.EMPTY, responseType);

        // Then
        assertThat(responseEntity.getBody()).isNotEmpty();
    }

    @Test
    public void when_checking_health_then_status_is_up() {
        // Given
        var actuatorUri = ActuatorUri.of(configurationServiceBaseUrl + "/actuator/health");

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }

    @Test
    public void when_healthy_then_configuration_for_the_gateway_exists() {
        // Given
        when_checking_health_then_status_is_up();
        var responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        // When
        var responseEntity =
                restTemplate.exchange(configurationServiceBaseUrl + "thule-gateway/application.yml", HttpMethod.GET, HttpEntity.EMPTY, responseType);

        // Then
        assertThat(responseEntity.getBody()).isNotEmpty();
    }

    @Test
    public void when_healthy_then_configuration_for_the_people_service_exists() {
        // Given
        when_checking_health_then_status_is_up();
        var responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        // When
        var responseEntity =
                restTemplate.exchange(configurationServiceBaseUrl + "thule-people-service/default", HttpMethod.GET, HttpEntity.EMPTY, responseType);

        // Then
        assertThat(responseEntity.getBody()).isNotEmpty();
    }

    @Test
    public void when_healthy_then_configuration_for_the_thule_service_exists() {
        // Given
        when_checking_health_then_status_is_up();
        var responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        // When
        var responseEntity =
                restTemplate.exchange(configurationServiceBaseUrl + "thule-thule-service/default", HttpMethod.GET, HttpEntity.EMPTY, responseType);

        // Then
        assertThat(responseEntity.getBody()).isNotEmpty();
    }
}