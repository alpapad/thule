package uk.co.serin.thule.gateway;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ConfigurationProperties("thule.gatewayservice")
@Getter
@Service
@Setter
@ToString
public class ApplicationProperties {
    private HealthCheck healthCheck = new HealthCheck();

    @Getter
    @Setter
    @ToString
    public static class HealthCheck {
        private List<String> services = Collections.emptyList();
        private long timeout = 5000;
    }
}
