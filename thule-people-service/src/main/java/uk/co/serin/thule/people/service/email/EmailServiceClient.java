package uk.co.serin.thule.people.service.email;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import uk.co.serin.thule.people.domain.model.email.Email;

@FeignClient("thule-email-service")
@FunctionalInterface
public interface EmailServiceClient {
    @PostMapping(path = "/emails", headers = "Content-Type=application/json")
    Email sendEmail(Email email);
}
