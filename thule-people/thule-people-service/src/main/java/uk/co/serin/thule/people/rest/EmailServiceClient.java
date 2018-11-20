package uk.co.serin.thule.people.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import uk.co.serin.thule.people.domain.email.Email;

@FeignClient("thule-email-service")
@FunctionalInterface
public interface EmailServiceClient {
    @PostMapping(path = "/emails", headers = "Content-Type=application/json")
    Email create(Email email);
}
