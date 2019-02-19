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

    public HealthCheck getHealthCheck() {
        return healthCheck;
    }

    public void setHealthCheck(HealthCheck healthCheck) {
        this.healthCheck = healthCheck;
    }

    public class HealthCheck {
        private List<String> services = Collections.emptyList();
        private long timeout = 5000;

        public List<String> getServices() {
            return services;
        }

        public void setServices(List<String> services) {
            this.services = services;
        }

        public long getTimeout() {
            return timeout;
        }

        public void setTimeout(long timeout) {
            this.timeout = timeout;
        }
    }
}
