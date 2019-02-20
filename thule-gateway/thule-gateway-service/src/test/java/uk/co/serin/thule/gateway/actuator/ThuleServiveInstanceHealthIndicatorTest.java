package uk.co.serin.thule.gateway.actuator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.actuate.health.Status;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ThuleServiveInstanceHealthIndicatorTest {
    @Mock
    private ResponseEntity<Map> response;
    private Map<String, Object> responseBody = new HashMap<>();
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ServiceInstance serviceInstance;
    @InjectMocks
    private ThuleServiceInstanceHealthIndicator sut;

    @Test
    public void when_body_status_is_down_then_health_status_is_down() throws InterruptedException, ExecutionException {
        //Given
        responseBody.put("status", Status.DOWN);

        ReflectionTestUtils.setField(sut, "restTemplate", restTemplate);

        given(restTemplate.exchange(anyString(), any(), any(), eq(Map.class))).willReturn(response);
        given(response.hasBody()).willReturn(true);
        given(response.getBody()).willReturn(responseBody);

        //When
        var result = sut.doServiceInstanceHealthCheck(serviceInstance);

        //Then
        assertThat(result).isNotNull();
        assertThat(result.get()).isEqualTo(Status.DOWN);
    }

    @Test
    public void when_body_status_is_null_then_health_status_is_down() throws InterruptedException, ExecutionException {
        //Given
        ReflectionTestUtils.setField(sut, "restTemplate", restTemplate);

        given(restTemplate.exchange(anyString(), any(), any(), eq(Map.class))).willReturn(response);
        given(response.hasBody()).willReturn(true);
        given(response.getBody()).willReturn(responseBody);

        //When
        var result = sut.doServiceInstanceHealthCheck(serviceInstance);

        //Then
        assertThat(result).isNotNull();
        assertThat(result.get()).isEqualTo(Status.DOWN);
    }

    @Test
    public void when_microservice_is_up_then_health_status_is_up() throws InterruptedException, ExecutionException {
        //Given
        responseBody.put("status", Status.UP);
        ReflectionTestUtils.setField(sut, "restTemplate", restTemplate);

        given(restTemplate.exchange(anyString(), any(), any(), eq(Map.class))).willReturn(response);
        given(response.hasBody()).willReturn(true);
        given(response.getBody()).willReturn(responseBody);

        //When
        var result = sut.doServiceInstanceHealthCheck(serviceInstance);

        //Then
        assertThat(result).isNotNull();
        assertThat(result.get()).isEqualTo(Status.UP);
    }

    @Test
    public void when_response_body_is_null_then_health_status_is_down() throws InterruptedException, ExecutionException {
        //Given
        ReflectionTestUtils.setField(sut, "restTemplate", restTemplate);

        given(restTemplate.exchange(anyString(), any(), any(), eq(Map.class))).willReturn(response);
        given(response.hasBody()).willReturn(false);

        //When
        var result = sut.doServiceInstanceHealthCheck(serviceInstance);

        //Then
        assertThat(result).isNotNull();
        assertThat(result.get()).isEqualTo(Status.DOWN);
    }
}
