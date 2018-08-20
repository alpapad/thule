package com.gohenry.admin.contract;

import com.gohenry.test.assertj.ActuatorUri;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.time.Duration;

import static com.gohenry.test.assertj.GoHenryAssertions.assertThat;

@ActiveProfiles("ctest")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminContractTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void is_status_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(restTemplate.getRootUri() + "/actuator/health"));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }
}
