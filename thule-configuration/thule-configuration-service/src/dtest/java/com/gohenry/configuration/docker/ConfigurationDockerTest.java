package uk.co.serin.thule.configuration.docker;

import uk.co.serin.thule.test.assertj.ActuatorUri;
import uk.co.serin.thule.utils.docker.DockerCompose;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigurationDockerTest {
    private static DockerCompose dockerCompose = new DockerCompose("src/dtest/docker/thule-configuration-docker-tests/docker-compose.yml");
    private String configurationServiceBaseUrl;

    @Autowired
    private Environment env;


    private RestTemplate restTemplate = new RestTemplate();

    @BeforeClass
    public static void setUpClass() throws IOException {
        dockerCompose.downAndUp();
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        dockerCompose.down();
    }

    @Test
    public void has_configuration_for_the_bank_transfer_service() {
        // Given
        health_status_is_up();
        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        // When
        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(configurationServiceBaseUrl + "thule-bank-transfer-service/default", HttpMethod.GET, HttpEntity.EMPTY, responseType);

        // Then
        assertThat(responseEntity.getBody()).isNotEmpty();
    }

    @Test
    public void health_status_is_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(configurationServiceBaseUrl + "/actuator/health"));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(java.time.Duration.ofMinutes(5)).hasStatus(Status.UP);
    }

    @Test
    public void has_configuration_for_the_card_application_service() {
        // Given
        health_status_is_up();
        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        // When
        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(configurationServiceBaseUrl + "thule-card-application-service/default", HttpMethod.GET, HttpEntity.EMPTY, responseType);

        // Then
        assertThat(responseEntity.getBody()).isNotEmpty();
    }

    @Test
    public void has_configuration_for_the_discovery_service() {
        // Given
        health_status_is_up();
        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        // When
        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(configurationServiceBaseUrl + "thule-discovery-service/default", HttpMethod.GET, HttpEntity.EMPTY, responseType);

        // Then
        assertThat(responseEntity.getBody()).isNotEmpty();
    }

    @Test
    public void has_configuration_for_the_gateway() {
        // Given
        health_status_is_up();
        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        // When
        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(configurationServiceBaseUrl + "thule-gateway/application.yml", HttpMethod.GET, HttpEntity.EMPTY, responseType);

        // Then
        assertThat(responseEntity.getBody()).isNotEmpty();
    }

    @Test
    public void has_configuration_for_the_statement_service() {
        // Given
        health_status_is_up();
        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        // When
        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(configurationServiceBaseUrl + "thule-statement-service/default", HttpMethod.GET, HttpEntity.EMPTY, responseType);

        // Then
        assertThat(responseEntity.getBody()).isNotEmpty();
    }

    @Before
    public void setUp() {
        // Create base url
        String configurationServiceApiHost = env.getRequiredProperty("thule.configurationservice.api.host");
        int configurationServiceApiPort = env.getRequiredProperty("thule.configurationservice.api.port", Integer.class);
        configurationServiceBaseUrl = "http://" + configurationServiceApiHost + ":" + configurationServiceApiPort;
    }

    @TestConfiguration
    static class ContainerTestConfiguration {
    }
}