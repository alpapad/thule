package uk.co.serin.thule.gateway.actuator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("itest")
@AutoConfigureWireMock(port = 0)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HealthCheckIntTest {
    @MockBean(name = "simpleDiscoveryClient")
    private DiscoveryClient discoveryClient;
    @LocalServerPort
    private int port;
    @Mock
    private ServiceInstance serviceInstance;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Value("${wiremock.server.port}")
    private int wireMockServerPort;

    @Test
    public void when_checking_health_then_response_should_be_ok() {
        // Given
        stubFor(get(
                urlEqualTo("/actuator/health")).
                                                       willReturn(aResponse().
                                                                                     withBodyFile("actuator-up-response.json").
                                                                                     withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE).
                                                                                     withStatus(HttpStatus.OK.value())));

        given(discoveryClient.getInstances("thule-admin-service")).willReturn(Collections.singletonList(serviceInstance));
        given(discoveryClient.getInstances("thule-authentication-service")).willReturn(Collections.singletonList(serviceInstance));
        given(discoveryClient.getInstances("thule-configuration-service")).willReturn(Collections.singletonList(serviceInstance));
        given(discoveryClient.getInstances("thule-discovery-service")).willReturn(Collections.singletonList(serviceInstance));
        given(discoveryClient.getInstances("thule-email-service")).willReturn(Collections.singletonList(serviceInstance));
        given(discoveryClient.getInstances("thule-people-service")).willReturn(Collections.singletonList(serviceInstance));
        given(serviceInstance.getUri()).willReturn(URI.create("http://localhost:" + wireMockServerPort));

        // When
        ResponseEntity<Map> responseEntity = testRestTemplate.getForEntity(String.format("http://localhost:%s/actuator/health", port), Map.class);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}