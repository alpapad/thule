package uk.co.serin.thule.email.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import uk.co.serin.thule.email.TestDataFactory;
import uk.co.serin.thule.email.domain.Email;

import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.mail.internet.MimeMessage;
import javax.validation.ValidationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
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
    public void given_an_exception_when_creating_an_email_then_a_email_service_exception_is_thrown() {
        // Given
        Email expectedEmail = TestDataFactory.buildEmail();

        given(mailSender.createMimeMessage()).willReturn(mimeMessage);
        doThrow(EmailServiceException.class).when(mailSender).send(any(MimeMessage.class));

        // When
        Throwable throwable = catchThrowable(() -> emailService.createEmail(expectedEmail));

        // Then
        assertThat(throwable).isInstanceOf(EmailServiceException.class);
    }

    @Test
    public void when_creating_an_email_then_an_email_is_sent_and_returned() throws ExecutionException, InterruptedException {
        // Given
        Email expectedEmail = TestDataFactory.buildEmail();

        given(mailSender.createMimeMessage()).willReturn(mimeMessage);

        // When
        Future<Email> emailFuture = emailService.createEmail(expectedEmail);

        // Then
        Email actualEmail = emailFuture.get();
        assertThat(actualEmail).isEqualToComparingFieldByField(expectedEmail);
    }

    @Test
    public void when_creating_an_email_without_a_from_then_an_email_is_sent() throws ExecutionException, InterruptedException {
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
    public void when_creating_an_email_without_any_recipients_then_a_validation_exception_is_thrown() {
        // Given
        Email expectedEmail = Email.builder().from("from@test.co.uk").subject("This is a test email").build();
        expectedEmail.setBody("This is the content");

        // When
        Throwable throwable = catchThrowable(() -> emailService.createEmail(expectedEmail));

        // Then
        assertThat(throwable).isInstanceOf(ValidationException.class);
    }

    @Test
    public void when_creating_an_email_without_bccs_then_an_email_is_sent() throws ExecutionException, InterruptedException {
        // Given
        Email expectedEmail = Email.builder().from("from@test.co.uk").subject("This is a test email").build();
        expectedEmail.setBody("This is the content");
        expectedEmail.setCcs(Collections.singleton("cc@test.co.uk"));
        expectedEmail.setTos(Collections.singleton("to@test.co.uk"));

        given(mailSender.createMimeMessage()).willReturn(mimeMessage);

        // When
        Future<Email> emailFuture = emailService.createEmail(expectedEmail);

        // Then
        Email actualEmail = emailFuture.get();
        assertThat(actualEmail).isEqualToComparingFieldByField(expectedEmail);
    }

    @Test
    public void when_creating_an_email_without_ccs_then_an_email_is_sent() throws ExecutionException, InterruptedException {
        // Given
        Email expectedEmail = Email.builder().from("from@test.co.uk").subject("This is a test email").build();
        expectedEmail.setBody("This is the content");
        expectedEmail.setBccs(Collections.singleton("bcc@test.co.uk"));
        expectedEmail.setTos(Collections.singleton("to@test.co.uk"));

        given(mailSender.createMimeMessage()).willReturn(mimeMessage);

        // When
        Future<Email> emailFuture = emailService.createEmail(expectedEmail);

        // Then
        Email actualEmail = emailFuture.get();
        assertThat(actualEmail).isEqualToComparingFieldByField(expectedEmail);
    }

    @Test
    public void when_creating_an_email_without_tos_then_an_email_is_sent() throws ExecutionException, InterruptedException {
        // Given
        Email expectedEmail = Email.builder().from("from@test.co.uk").subject("This is a test email").build();
        expectedEmail.setBody("This is the content");
        expectedEmail.setBccs(Collections.singleton("bcc@test.co.uk"));
        expectedEmail.setCcs(Collections.singleton("cc@test.co.uk"));

        given(mailSender.createMimeMessage()).willReturn(mimeMessage);

        // When
        Future<Email> emailFuture = emailService.createEmail(expectedEmail);

        // Then
        Email actualEmail = emailFuture.get();
        assertThat(actualEmail).isEqualToComparingFieldByField(expectedEmail);
    }

    @Test
    public void when_creating_an_email_with_only_bcc_then_an_email_is_sent() throws ExecutionException, InterruptedException {
        // Given
        Email expectedEmail = Email.builder().from("from@test.co.uk").subject("This is a test email").build();
        expectedEmail.setBody("This is the content");
        expectedEmail.setBccs(Collections.singleton("bcc@test.co.uk"));

        given(mailSender.createMimeMessage()).willReturn(mimeMessage);

        // When
        Future<Email> emailFuture = emailService.createEmail(expectedEmail);

        // Then
        Email actualEmail = emailFuture.get();
        assertThat(actualEmail).isEqualToComparingFieldByField(expectedEmail);
    }
}
