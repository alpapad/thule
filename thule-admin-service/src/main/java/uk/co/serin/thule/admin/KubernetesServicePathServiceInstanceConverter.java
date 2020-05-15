package uk.co.serin.thule.admin;

import de.codecentric.boot.admin.server.cloud.discovery.KubernetesServiceInstanceConverter;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.springframework.util.StringUtils.isEmpty;

public class KubernetesServicePathServiceInstanceConverter extends KubernetesServiceInstanceConverter {
    @Override
    protected URI getServiceUrl(ServiceInstance instance) {
        var servicePath = instance.getMetadata().get("service-path");
        if (!isEmpty(servicePath)) {
            return UriComponentsBuilder.fromUri(instance.getUri()).path(servicePath).build().toUri();
        }

        return super.getServiceUrl(instance);
    }
}