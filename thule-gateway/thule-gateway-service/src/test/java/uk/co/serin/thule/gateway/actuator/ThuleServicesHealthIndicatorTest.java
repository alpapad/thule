package uk.co.serin.thule.gateway.actuator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import uk.co.serin.thule.gateway.ApplicationProperties;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ThuleServicesHealthIndicatorTest {

    private ThuleServicesHealthIndicator sut;
    @Mock
    private ApplicationProperties applicationProperties;
    @Mock
    private Health.Builder builder;
    @Mock
    private DiscoveryClient discoveryClient;
    @Mock
    private Future<Status> futureStatus;
    @Mock
    private ThuleServiceInstanceHealthIndicator thuleServiceInstanceHealthIndicator;
    @Mock
    private ApplicationProperties.HealthCheck healthCheck;
    @Mock
    private ServiceInstance serviceInstance;

    @Before
    public void setUp() {
        given(applicationProperties.getHealthCheck()).willReturn(healthCheck);
        given(healthCheck.getTimeout()).willReturn(2000L);
        sut = new ThuleServicesHealthIndicator(thuleServiceInstanceHealthIndicator, applicationProperties, discoveryClient);
    }

    @Test
    public void when_all_microservices_are_up_then_health_status_should_be_up() throws Exception {
        //Given
        List<String> serviceIds = Stream.of("Instanceone", "Instancetwo").collect(Collectors.toList());
        List<ServiceInstance> serviceInstances = Collections.singletonList(serviceInstance);

        given(healthCheck.getServices()).willReturn(serviceIds);
        given(discoveryClient.getInstances(anyString())).willReturn(serviceInstances).willReturn(serviceInstances);
        given(thuleServiceInstanceHealthIndicator.doServiceInstanceHealthCheck(serviceInstance)).willReturn(futureStatus);
        given(futureStatus.isDone()).willReturn(false).willReturn(true).willReturn(true);
        given(futureStatus.get()).willReturn(Status.UP);
        given(builder.up()).willReturn(builder);

        //When
        sut.doHealthCheck(builder);

        // Then
        verify(builder).up();
    }

    @Test
    public void when_an_interrupted_exception_then_an_ilegal_state_exception_is_thrown() throws ExecutionException, InterruptedException {
        //Given
        List<String> serviceIds = Stream.of("Instanceone").collect(Collectors.toList());
        List<ServiceInstance> serviceInstances = Collections.singletonList(serviceInstance);

        given(healthCheck.getServices()).willReturn(serviceIds);
        given(discoveryClient.getInstances(anyString())).willReturn(serviceInstances);
        given(thuleServiceInstanceHealthIndicator.doServiceInstanceHealthCheck(serviceInstance)).willReturn(futureStatus);
        given(futureStatus.isDone()).willReturn(true).willReturn(true);
        given(futureStatus.get()).willThrow(InterruptedException.class);

        //When
        try {
            sut.doHealthCheck(builder);
            fail();
        } catch (IllegalStateException e) {
            //Then
            assertThat(e.getMessage()).isEqualTo("Unable to determine the status of a micro service");
        }
    }

    @Test
    public void when_no_microservice_instances_exist_then_an_ilegal_state_exception_is_thrown() {
        //Given
        List<String> serviceIds = Stream.of("Instanceone", "Instancetwo").collect(Collectors.toList());
        List<ServiceInstance> serviceInstances = Collections.emptyList();

        given(healthCheck.getServices()).willReturn(serviceIds);
        given(discoveryClient.getInstances(anyString())).willReturn(serviceInstances);

        //When
        //When
        try {
            sut.doHealthCheck(builder);
            fail();
        } catch (IllegalStateException e) {
            //Then
            assertThat(e.getMessage()).isEqualTo("No instances of [Instanceone] have been registered with the discovery service");
        }
    }

    @Test
    public void when_one_microservice_is_down_then_health_status_should_be_down() throws Exception {
        //Given
        List<String> serviceIds = Stream.of("Instanceone", "Instancetwo").collect(Collectors.toList());
        List<ServiceInstance> serviceInstances = Collections.singletonList(serviceInstance);

        given(healthCheck.getServices()).willReturn(serviceIds);
        given(discoveryClient.getInstances(anyString())).willReturn(serviceInstances);
        given(thuleServiceInstanceHealthIndicator.doServiceInstanceHealthCheck(serviceInstance)).willReturn(futureStatus);
        given(futureStatus.isDone()).willReturn(false).willReturn(false).willReturn(true);
        given(futureStatus.get()).willReturn(Status.DOWN);
        given(builder.down()).willReturn(builder);

        //When
        sut.doHealthCheck(builder);

        //Then
        verify(builder).down();
    }

    @Test
    public void when_one_microservice_is_down_then_other_microservices_futures_should_be_cancelled() throws Exception {
        //Given
        List<String> serviceIds = Stream.of("Instanceone", "Instancetwo").collect(Collectors.toList());
        List<ServiceInstance> serviceInstances = Collections.singletonList(serviceInstance);

        given(healthCheck.getServices()).willReturn(serviceIds);
        given(discoveryClient.getInstances(anyString())).willReturn(serviceInstances);
        given(thuleServiceInstanceHealthIndicator.doServiceInstanceHealthCheck(serviceInstance)).willReturn(futureStatus);
        given(futureStatus.isDone()).willReturn(true).willReturn(true).willReturn(false);
        given(futureStatus.get()).willReturn(Status.DOWN);
        given(builder.down()).willReturn(builder);

        //When
        sut.doHealthCheck(builder);

        //Then
        verify(futureStatus).cancel(true);
    }
}