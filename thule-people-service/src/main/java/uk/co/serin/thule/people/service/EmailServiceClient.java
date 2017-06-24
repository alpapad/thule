package uk.co.serin.thule.people.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.co.serin.thule.people.domain.email.Email;

@FeignClient("thule-email-service")
@FunctionalInterface
public interface EmailServiceClient {
    @RequestMapping(path = "/emails", headers = "Content-Type=application/json", method = RequestMethod.POST)
    Email create(Email email);
}
