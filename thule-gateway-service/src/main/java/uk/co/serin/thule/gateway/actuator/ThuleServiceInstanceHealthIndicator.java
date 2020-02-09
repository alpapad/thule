package uk.co.serin.thule.gateway.actuator;

import org.springframework.boot.actuate.health.Status;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.concurrent.Future;

@Service
public class ThuleServiceInstanceHealthIndicator {
    private WebClient webClient = WebClient.create();

    /**
     * Performs a health check on a single service instance
     * <p>
     * Forms a url using the single instance's information
     * performs a rest exchange to it's actuator endpoint
     * checks the response is not null and that status is up to return UP, otherwise return DOWN
     */
    @Async
    public Future<Status> doServiceInstanceHealthCheck(ServiceInstance instance) {
        var url = String.format("%s/actuator/health", instance.getUri());

        var body = webClient.mutate().baseUrl(url).build().get().retrieve()
                            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                            }).block();

        if (body != null && body.get("status") != null && body.get("status").toString().equals(Status.UP.getCode())) {
            return new AsyncResult<>(Status.UP);
        } else {
            return new AsyncResult<>(Status.DOWN);
        }
    }
}
