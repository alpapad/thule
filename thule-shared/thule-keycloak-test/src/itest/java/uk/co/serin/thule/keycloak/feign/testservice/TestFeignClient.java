package uk.co.serin.thule.keycloak.feign.testservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("thule-test-service")
@FunctionalInterface
public interface TestFeignClient {
    @RequestMapping(path = "/hello", headers = "Content-Type=application/json")
    String hello();
}
