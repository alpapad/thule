package uk.co.serin.thule.email.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.co.serin.thule.email.domain.Email;
import uk.co.serin.thule.email.service.EmailService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/emails")
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<Email> createEmail(@RequestBody @Valid Email email) {
        emailService.createEmail(email);
        return new ResponseEntity<>(email, HttpStatus.ACCEPTED);
    }
}
