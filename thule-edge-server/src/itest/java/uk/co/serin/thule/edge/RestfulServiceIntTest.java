package uk.co.serin.thule.edge;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.config.location=classpath:/config/${spring.application.name}/", "spring.cloud.bootstrap.location=classpath:/"})
@RunWith(SpringRunner.class)
public class RestfulServiceIntTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void is_healthy() {
        // Given
        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        // When
        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange("/health", HttpMethod.GET, null, responseType);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().get("status")).isEqualTo(Status.UP.getCode());
    }
}
