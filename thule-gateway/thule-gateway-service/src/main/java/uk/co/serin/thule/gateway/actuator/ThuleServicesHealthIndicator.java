package uk.co.serin.thule.gateway.actuator;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import uk.co.serin.thule.gateway.ApplicationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Retrieves all service ids as a list of Strings
 * Get all ServiceInstances associated with a particular serviceId as a list ServiceInstances
 * for each ServiceInstance, builds URL based of ServiceInstance object data
 * retrieves Health information and if Health is not good on any, returns down
 * else returns Health is up
 */
@Service("thuleServices")
public class ThuleServicesHealthIndicator extends AbstractHealthIndicator {

    private ApplicationProperties applicationProperties;
    private DiscoveryClient discoveryClient;
    private ThuleServiceInstanceHealthIndicator thuleServiceInstanceHealthIndicator;
    private RetryTemplate retryTemplate = new RetryTemplate();

    /**
     * @param thuleServiceInstanceHealthIndicator Injects the ThuleServiceInstanceHealthIndicator as a bean as so to enable the @Async functionality.
     */
    public ThuleServicesHealthIndicator(ThuleServiceInstanceHealthIndicator thuleServiceInstanceHealthIndicator,
                                          ApplicationProperties applicationProperties, DiscoveryClient discoveryClient) {
        this.thuleServiceInstanceHealthIndicator = thuleServiceInstanceHealthIndicator;
        this.applicationProperties = applicationProperties;
        this.discoveryClient = discoveryClient;

        var retryPolicy = new TimeoutRetryPolicy();
        retryPolicy.setTimeout(applicationProperties.getHealthCheck().getTimeout());

        retryTemplate.setBackOffPolicy(new FixedBackOffPolicy());
        retryTemplate.setRetryPolicy(retryPolicy);
    }

    /**
     * builds list of ServiceInstances and performs healthchecks on each one, returning status.DOWN if any are down.
     *
     * @param builder Starts all health checks
     *                checks list isn't empty
     *                gets the overall status of the health checks
     *                builds depending on result of overall
     */
    @Override
    protected void doHealthCheck(Health.Builder builder) throws InterruptedException {
        var instanceFutures = startAllHealthChecks();

        var statusOfMicroServices = getStatusOfMicroServices(instanceFutures);
        if (statusOfMicroServices.equals(Status.DOWN)) {
            cancelRemainingFutures(instanceFutures);
            builder.down().build();
        } else {
            builder.up().build();
        }
    }

    private List<Future<Status>> startAllHealthChecks() {
        List<Future<Status>> instanceFutures = new ArrayList<>();
        var serviceIds = applicationProperties.getHealthCheck().getServices();

        for (var serviceId : serviceIds) {
            var serviceInstances = discoveryClient.getInstances(serviceId);
            if (serviceInstances.isEmpty()) {
                throw new IllegalStateException(String.format("No instances of [%s] have been registered with the discovery service", serviceId));
            }
            for (var serviceInstance : serviceInstances) {
                var statusFuture = this.thuleServiceInstanceHealthIndicator.doServiceInstanceHealthCheck(serviceInstance);
                instanceFutures.add(statusFuture);
            }
        }
        return instanceFutures;
    }

    private Status getStatusOfMicroServices(List<Future<Status>> instanceFutures) throws InterruptedException {
        return retryTemplate.execute(context -> {
            try {
                var allServicesAreUp = true;

                for (var instanceFuture : instanceFutures) {
                    if (instanceFuture.isDone()) {
                        if (instanceFuture.get().equals(Status.DOWN)) {
                            return Status.DOWN;
                        }
                    } else {
                        allServicesAreUp = false;
                    }
                }

                return checkAllServicesAreUp(allServicesAreUp);
            } catch (ExecutionException e) {
                return Status.DOWN;
            }
        });
    }

    private void cancelRemainingFutures(List<Future<Status>> statuses) {
        for (var status : statuses) {
            if (!status.isDone()) {
                status.cancel(true);
            }
        }
    }

    private Status checkAllServicesAreUp(boolean allServicesAreUp) {
        if (allServicesAreUp) {
            return Status.UP;
        } else {
            throw new IllegalStateException("At least one micro service is down");
        }
    }
}
