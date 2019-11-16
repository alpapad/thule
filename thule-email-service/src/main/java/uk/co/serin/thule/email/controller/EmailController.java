package uk.co.serin.thule.email.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import uk.co.serin.thule.email.domain.model.Email;
import uk.co.serin.thule.email.service.EmailService;
import uk.co.serin.thule.utils.trace.TracePublicMethods;

import javax.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@RestController
@RequestMapping("/emails")
@TracePublicMethods
public class EmailController {
    @NonNull
    private final EmailService emailService;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Email createEmail(@RequestBody @Valid Email email) {
        emailService.send(email);
        return email;
    }
}
