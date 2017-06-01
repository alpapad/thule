package uk.co.serin.thule.email.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import uk.co.serin.thule.email.domain.Email;

import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {
    @InjectMocks
    private EmailService emailService;
    @Mock
    private JavaMailSender mailSender;

    @Test
    public void create_an_email() throws ExecutionException, InterruptedException {
        // Given
        Email expectedEmail = buildEmail();

        // When
        Future<Email> emailFuture = emailService.createEmail(expectedEmail);

        // Then
        Email actualEmail = emailFuture.get();
        assertThat(actualEmail).isEqualToComparingFieldByField(expectedEmail);
    }

    public Email buildEmail() {
        Email email = new Email("from@test.co.uk", "This is a test email");
        email.setBody("This is the content");
        email.addTos(Collections.singleton("to@test.co.uk"));

        return email;
    }

    @Test(expected = EmailServiceException.class)
    public void create_an_email_that_throws_a_service_exception() {
        // Given
        Email expectedEmail = buildEmail();

        doThrow(EmailServiceException.class).when(mailSender).send(any(SimpleMailMessage.class));

        // When
        emailService.createEmail(expectedEmail);

        // Then - see the expected in @Test
    }

    @Test
    public void create_an_email_with_ccs_and_bccs_without_tos() throws ExecutionException, InterruptedException {
        // Given
        Email expectedEmail = new Email("from@test.co.uk", "This is a test email");
        expectedEmail.setBody("This is the content");
        expectedEmail.addBccs(Collections.singleton("bcc@test.co.uk"));
        expectedEmail.addCcs(Collections.singleton("cc@test.co.uk"));

        // When
        Future<Email> emailFuture = emailService.createEmail(expectedEmail);

        // Then
        Email actualEmail = emailFuture.get();
        assertThat(actualEmail).isEqualToComparingFieldByField(expectedEmail);
    }

    @Test(expected = EmailServiceValidationException.class)
    public void create_an_email_without_any_recipients() {
        // Given
        Email expectedEmail = new Email("from@test.co.uk", "This is a test email");
        expectedEmail.setBody("This is the content");

        // When
        emailService.createEmail(expectedEmail);

        // Then - see the expected in the @Test
    }
}
