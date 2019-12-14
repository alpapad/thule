package uk.co.serin.thule.people.service.email;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import uk.co.serin.thule.people.domain.model.email.Email;
import uk.co.serin.thule.utils.trace.TracePublicMethods;

import java.util.concurrent.Future;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
@TracePublicMethods
public class EmailServiceClientAsync {
    private EmailServiceClient emailServiceClient;

    @Async
    public Future<Email> sendEmail(Email email) {
        return new AsyncResult<>(emailServiceClient.sendEmail(email));
    }
}

