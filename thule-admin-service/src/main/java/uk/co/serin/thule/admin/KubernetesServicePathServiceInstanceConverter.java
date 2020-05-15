package uk.co.serin.thule.admin;

import de.codecentric.boot.admin.server.cloud.discovery.KubernetesServiceInstanceConverter;

import org.springframework.cloud.client.ServiceInstance;

import java.net.URI;

import static org.springframework.util.StringUtils.isEmpty;

public class KubernetesServicePathServiceInstanceConverter extends KubernetesServiceInstanceConverter {
    @Override
    protected URI getServiceUrl(ServiceInstance instance) {
        var servicePath = instance.getMetadata().get("service-path");
        if (!isEmpty(servicePath)) {
            return URI.create(servicePath);
        }

        return super.getServiceUrl(instance);
    }
}