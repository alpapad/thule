package com.gohenry.gateway.actuator;

import org.springframework.boot.actuate.health.Status;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.Future;

@Service
public class GohenryServiceInstanceHealthIndicator {
    private RestTemplate restTemplate = new RestTemplate();

    /**
     * Performs a health check on a single service instance
     * <p>
     * Forms a url using the single instance's information
     * performs a rest exchange to it's actuator endpoint
     * checks the response is not null and that status is up to return UP, otherwise return DOWN
     */
    @Async
    public Future<Status> doServiceInstanceHealthCheck(ServiceInstance instance) {
        String url = String.format("%s/actuator/health", instance.getUri());

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, Map.class);

        if (response.hasBody() && response.getBody().get("status") != null && response.getBody().get("status").toString().equals(Status.UP.getCode())) {
            return new AsyncResult<>(Status.UP);
        } else {
            return new AsyncResult<>(Status.DOWN);
        }
    }
}
