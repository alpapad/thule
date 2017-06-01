package uk.co.serin.thule.email.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import uk.co.serin.thule.email.domain.Email;
import uk.co.serin.thule.email.service.EmailService;
import uk.co.serin.thule.email.service.EmailServiceValidationException;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/emails")
class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Email> createEmail(@RequestBody @Valid Email email) {
        emailService.createEmail(email);
        return new ResponseEntity<>(email, HttpStatus.ACCEPTED);
    }

    @ExceptionHandler(EmailServiceValidationException.class)
    public ResponseEntity<String> validationExceptionHandler(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
