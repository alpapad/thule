package uk.co.serin.thule.spring.boot.starter.security.oauth2.testservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@FeignClient("gohenry-test-service")
@FunctionalInterface
public interface TestFeignClient {
    @RequestMapping(path = "/authentication", headers = "Content-Type=application/json")
    Map<String, Object> getAuthentication();
}
