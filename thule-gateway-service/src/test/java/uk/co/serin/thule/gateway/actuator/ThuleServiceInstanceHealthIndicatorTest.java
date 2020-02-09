package uk.co.serin.thule.gateway.actuator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.actuate.health.Status;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ThuleServiceInstanceHealthIndicatorTest {
    @Mock
    private Mono<Map<String, Object>> mono;
    @Mock
    private RequestHeadersUriSpec requestBodyUriSpec;
    private Map<String, Object> responseBody = new HashMap<>();
    @Mock
    private ResponseSpec responseSpec;
    @Mock
    private ServiceInstance serviceInstance;
    @InjectMocks
    private ThuleServiceInstanceHealthIndicator sut;
    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.Builder webClientBuilder;

    @Test
    public void when_body_status_is_down_then_health_status_is_down() throws InterruptedException, ExecutionException {
        // Given
        responseBody.put("status", Status.DOWN);

        ReflectionTestUtils.setField(sut, "webClient", webClient);

        given(webClient.mutate()).willReturn(webClientBuilder);
        given(webClientBuilder.baseUrl(anyString())).willReturn(webClientBuilder);
        given(webClientBuilder.build()).willReturn(webClient);
        given(webClient.get()).willReturn(requestBodyUriSpec);
        given(requestBodyUriSpec.retrieve()).willReturn(responseSpec);
        given(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).willReturn(mono);
        given(mono.block()).willReturn(responseBody);

        // When
        var status = sut.doServiceInstanceHealthCheck(serviceInstance);

        // Then
        assertThat(status).isNotNull();
        assertThat(status.get()).isEqualTo(Status.DOWN);
    }

    @Test
    public void when_body_status_is_null_then_health_status_is_down() throws InterruptedException, ExecutionException {
        // Given
        ReflectionTestUtils.setField(sut, "webClient", webClient);

        given(webClient.mutate()).willReturn(webClientBuilder);
        given(webClientBuilder.baseUrl(anyString())).willReturn(webClientBuilder);
        given(webClientBuilder.build()).willReturn(webClient);
        given(webClient.get()).willReturn(requestBodyUriSpec);
        given(requestBodyUriSpec.retrieve()).willReturn(responseSpec);
        given(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).willReturn(mono);
        given(mono.block()).willReturn(responseBody);

        // When
        var result = sut.doServiceInstanceHealthCheck(serviceInstance);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.get()).isEqualTo(Status.DOWN);
    }

    @Test
    public void when_microservice_is_up_then_health_status_is_up() throws InterruptedException, ExecutionException {
        // Given
        responseBody.put("status", Status.UP);
        ReflectionTestUtils.setField(sut, "webClient", webClient);

        given(webClient.mutate()).willReturn(webClientBuilder);
        given(webClientBuilder.baseUrl(anyString())).willReturn(webClientBuilder);
        given(webClientBuilder.build()).willReturn(webClient);
        given(webClient.get()).willReturn(requestBodyUriSpec);
        given(requestBodyUriSpec.retrieve()).willReturn(responseSpec);
        given(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).willReturn(mono);
        given(mono.block()).willReturn(responseBody);

        // When
        var result = sut.doServiceInstanceHealthCheck(serviceInstance);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.get()).isEqualTo(Status.UP);
    }

    @Test
    public void when_response_body_is_null_then_health_status_is_down() throws InterruptedException, ExecutionException {
        // Given
        ReflectionTestUtils.setField(sut, "webClient", webClient);

        given(webClient.mutate()).willReturn(webClientBuilder);
        given(webClientBuilder.baseUrl(anyString())).willReturn(webClientBuilder);
        given(webClientBuilder.build()).willReturn(webClient);
        given(webClient.get()).willReturn(requestBodyUriSpec);
        given(requestBodyUriSpec.retrieve()).willReturn(responseSpec);
        given(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).willReturn(mono);
        given(mono.block()).willReturn(null);

        // When
        var result = sut.doServiceInstanceHealthCheck(serviceInstance);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.get()).isEqualTo(Status.DOWN);
    }
}
