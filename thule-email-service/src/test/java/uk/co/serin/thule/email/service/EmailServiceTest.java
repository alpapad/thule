package uk.co.serin.thule.email.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import uk.co.serin.thule.email.datafactories.TestDataFactory;
import uk.co.serin.thule.email.domain.Email;

import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.mail.internet.MimeMessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {
    @InjectMocks
    private EmailService emailService;
    @Mock
    private JavaMailSender mailSender;
    @Mock
    private MimeMessage mimeMessage;

    @Test
    public void create_an_email() throws ExecutionException, InterruptedException {
        // Given
        Email expectedEmail = TestDataFactory.buildEmail();

        given(mailSender.createMimeMessage()).willReturn(mimeMessage);

        // When
        Future<Email> emailFuture = emailService.createEmail(expectedEmail);

        // Then
        Email actualEmail = emailFuture.get();
        assertThat(actualEmail).isEqualToComparingFieldByField(expectedEmail);
    }

    @Test(expected = EmailServiceException.class)
    public void create_an_email_that_throws_a_service_exception() {
        // Given
        Email expectedEmail = TestDataFactory.buildEmail();

        given(mailSender.createMimeMessage()).willReturn(mimeMessage);
        doThrow(EmailServiceException.class).when(mailSender).send(any(MimeMessage.class));

        // When
        emailService.createEmail(expectedEmail);

        // Then - see the expected in @Test
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

    @Test
    public void create_an_email_without_bccs() throws ExecutionException, InterruptedException {
        // Given
        Email expectedEmail = new Email("from@test.co.uk", "This is a test email");
        expectedEmail.setBody("This is the content");
        expectedEmail.addCcs(Collections.singleton("cc@test.co.uk"));
        expectedEmail.addTos(Collections.singleton("to@test.co.uk"));

        given(mailSender.createMimeMessage()).willReturn(mimeMessage);

        // When
        Future<Email> emailFuture = emailService.createEmail(expectedEmail);

        // Then
        Email actualEmail = emailFuture.get();
        assertThat(actualEmail).isEqualToComparingFieldByField(expectedEmail);
    }

    @Test
    public void create_an_email_without_from() throws ExecutionException, InterruptedException {
        // Given
        Email expectedEmail = TestDataFactory.buildEmail();
        ReflectionTestUtils.setField(expectedEmail, "from", null);

        given(mailSender.createMimeMessage()).willReturn(mimeMessage);

        // When
        Future<Email> emailFuture = emailService.createEmail(expectedEmail);

        // Then
        Email actualEmail = emailFuture.get();
        assertThat(actualEmail).isEqualToComparingFieldByField(expectedEmail);
    }

    @Test
    public void create_an_email_without_ccs() throws ExecutionException, InterruptedException {
        // Given
        Email expectedEmail = new Email("from@test.co.uk", "This is a test email");
        expectedEmail.setBody("This is the content");
        expectedEmail.addBccs(Collections.singleton("bcc@test.co.uk"));
        expectedEmail.addTos(Collections.singleton("to@test.co.uk"));

        given(mailSender.createMimeMessage()).willReturn(mimeMessage);

        // When
        Future<Email> emailFuture = emailService.createEmail(expectedEmail);

        // Then
        Email actualEmail = emailFuture.get();
        assertThat(actualEmail).isEqualToComparingFieldByField(expectedEmail);
    }

    @Test
    public void create_an_email_without_tos() throws ExecutionException, InterruptedException {
        // Given
        Email expectedEmail = new Email("from@test.co.uk", "This is a test email");
        expectedEmail.setBody("This is the content");
        expectedEmail.addBccs(Collections.singleton("bcc@test.co.uk"));
        expectedEmail.addCcs(Collections.singleton("cc@test.co.uk"));

        given(mailSender.createMimeMessage()).willReturn(mimeMessage);

        // When
        Future<Email> emailFuture = emailService.createEmail(expectedEmail);

        // Then
        Email actualEmail = emailFuture.get();
        assertThat(actualEmail).isEqualToComparingFieldByField(expectedEmail);
    }
}
