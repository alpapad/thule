package uk.co.serin.thule.discovery;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RestfulServiceIntTest {
    @Autowired
    private Environment env;
    private RestTemplate restTemplate = new RestTemplate();
    private String url;

    @Test
    public void isHealthy() {
        // Given
        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        // When
        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, responseType);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().get("status")).isEqualTo(Status.UP.getCode());
    }

    @Before
    public void setUp() {
        url = "http://localhost:" + env.getRequiredProperty("server.port", Integer.class) + "/health";
    }
}
