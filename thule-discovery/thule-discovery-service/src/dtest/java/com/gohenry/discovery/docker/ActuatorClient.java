package com.gohenry.discovery.docker;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@FeignClient("gohenry-discovery-service")
@FunctionalInterface
public interface ActuatorClient {
    @RequestMapping(path = "/actuator/health", headers = "Content-Type=application/json")
    Map<String, Object> health();
}
