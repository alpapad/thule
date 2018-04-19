package uk.co.serin.thule.discovery.e2e;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@FeignClient("gohenry-discovery-service")
@FunctionalInterface
public interface EchoServiceClient {
    @RequestMapping(path = "/echo/{message}", headers = "Content-Type=application/json")
    Map<String, String> echo(@PathVariable("message") String message);
}
