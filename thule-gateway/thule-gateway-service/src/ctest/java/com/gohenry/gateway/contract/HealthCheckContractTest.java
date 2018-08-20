package com.gohenry.gateway.contract;

import org.awaitility.Awaitility;
import org.awaitility.Duration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.cloud.contract.wiremock.WireMockConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.pollinterval.FibonacciPollInterval.fibonacci;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("ctest")
@AutoConfigureWireMock(port = 0)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HealthCheckContractTest {
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
    public void given_asynchronous_processing_when_checking_health_then_should_respond_within_5_seconds() {
        // Given
        stubFor(get(
                urlEqualTo("/actuator/health")).
                willReturn(aResponse().
                        withFixedDelay(2000).
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
        List<ResponseEntity<Map>> responseEntitities = new ArrayList<>();
        Awaitility.given().ignoreExceptions().pollInterval(fibonacci()).
                await().timeout(Duration.FIVE_SECONDS). // Allow up to 10 seconds to complete, if it takes longer, asynchronous process is probably not working
                untilAsserted(() -> responseEntitities.add(testRestTemplate.getForEntity(String.format("http://localhost:%s/actuator/health", port), Map.class)));

        // Then
        verify(getRequestedFor(urlPathEqualTo("/actuator/health")));
        assertThat(responseEntitities).hasSize(1);
        assertThat(responseEntitities.get(0).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @TestConfiguration
    static class BankTransferRequestIntTestConfiguration {
        @Bean
        public WireMockConfigurationCustomizer wireMockConfigurationCustomizer() {
            return config -> config.containerThreads(20); // Defaults to 10 threads which is not enough to test for asynchronous processing
        }
    }
}