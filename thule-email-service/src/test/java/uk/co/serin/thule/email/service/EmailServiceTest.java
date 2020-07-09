package uk.co.serin.thule.email.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;

import uk.co.serin.thule.email.domain.model.Attachment;
import uk.co.serin.thule.email.domain.model.Email;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.mail.internet.MimeMessage;
import javax.validation.ValidationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    @InjectMocks
    private EmailService emailService;
    @Mock
    private JavaMailSender mailSender;
    @Mock
    private MimeMessage mimeMessage;

    @Test
    void given_an_exception_when_sending_an_email_then_a_email_service_exception_is_thrown() {
        // Given
        var expectedEmail =
                Email.builder().body("This is the content").from("from@test.co.uk").subject("This is a test email").tos(Set.of("to@test.co.uk"))
                     .build();

        given(mailSender.createMimeMessage()).willReturn(mimeMessage);
        doThrow(MailSendException.class).when(mailSender).send(any(MimeMessage.class));

        // When
        var throwable = catchThrowable(() -> emailService.send(expectedEmail));

        // Then
        assertThat(throwable).isInstanceOf(EmailServiceException.class);
    }

    @Test
    void when_sending_an_email_with_all_fields_set_then_an_email_is_sent_and_returned() throws ExecutionException, InterruptedException {
        // Given
        var attachments = Set.of(Attachment.builder().content("This is a test attachment").label("test-attachment.txt").build());
        var expectedEmail = Email.builder().attachments(attachments).bccs(Set.of("bcc@test.co.uk")).body("This is a test body")
                                 .ccs(Set.of("ccs@test.co.uk")).from("from@test.co.uk").subject("Test subject")
                                 .tos(Stream.of("to1@test.co.uk", "to2@test.co.uk", "to3@test.co.uk").collect(Collectors.toSet())).build();

        given(mailSender.createMimeMessage()).willReturn(mimeMessage);

        // When
        var emailFuture = emailService.send(expectedEmail);

        // Then
        var actualEmail = emailFuture.get();
        assertThat(actualEmail).isEqualToComparingFieldByField(expectedEmail);
    }

    @Test
    void when_sending_an_email_with_only_bcc_then_an_email_is_sent() throws ExecutionException, InterruptedException {
        // Given
        var expectedEmail = Email.builder().bccs(Set.of("bcc@test.co.uk")).body("This is the content").from("from@test.co.uk")
                                 .subject("This is a test email").build();

        given(mailSender.createMimeMessage()).willReturn(mimeMessage);

        // When
        var emailFuture = emailService.send(expectedEmail);

        // Then
        var actualEmail = emailFuture.get();
        assertThat(actualEmail).isEqualToComparingFieldByField(expectedEmail);
    }

    @Test
    void when_sending_an_email_without_a_from_then_an_email_is_sent() throws ExecutionException, InterruptedException {
        // Given
        var expectedEmail = Email.builder().body("This is the content").subject("This is a test email").tos(Set.of("to@test.co.uk")).build();

        given(mailSender.createMimeMessage()).willReturn(mimeMessage);

        // When
        var emailFuture = emailService.send(expectedEmail);

        // Then
        var actualEmail = emailFuture.get();
        assertThat(actualEmail).isEqualToComparingFieldByField(expectedEmail);
    }

    @Test
    void when_sending_an_email_without_any_recipients_then_a_validation_exception_is_thrown() {
        // Given
        var email = Email.builder().body("This is the content").from("from@test.co.uk").subject("This is a test email").build();

        // When
        var throwable = catchThrowable(() -> emailService.send(email));

        // Then
        assertThat(throwable).isInstanceOf(ValidationException.class);
    }

    @Test
    void when_sending_an_email_without_bccs_then_an_email_is_sent() throws ExecutionException, InterruptedException {
        // Given
        var expectedEmail =
                Email.builder().body("This is the content").ccs(Set.of("cc@test.co.uk")).from("from@test.co.uk").subject("This is a test email")
                     .tos(Set.of("to@test.co.uk")).build();

        given(mailSender.createMimeMessage()).willReturn(mimeMessage);

        // When
        var emailFuture = emailService.send(expectedEmail);

        // Then
        var actualEmail = emailFuture.get();
        assertThat(actualEmail).isEqualToComparingFieldByField(expectedEmail);
    }

    @Test
    void when_sending_an_email_without_ccs_then_an_email_is_sent() throws ExecutionException, InterruptedException {
        // Given
        var expectedEmail = Email.builder().bccs(Set.of("bcc@test.co.uk")).body("This is the content").from("from@test.co.uk")
                                 .subject("This is a test email").tos(Set.of("to@test.co.uk")).build();

        given(mailSender.createMimeMessage()).willReturn(mimeMessage);

        // When
        var emailFuture = emailService.send(expectedEmail);

        // Then
        var actualEmail = emailFuture.get();
        assertThat(actualEmail).isEqualToComparingFieldByField(expectedEmail);
    }

    @Test
    void when_sending_an_email_without_tos_then_an_email_is_sent() throws ExecutionException, InterruptedException {
        // Given
        var expectedEmail =
                Email.builder().bccs(Set.of("bcc@test.co.uk")).body("This is the content").ccs(Set.of("cc@test.co.uk"))
                     .from("from@test.co.uk").subject("This is a test email").build();

        given(mailSender.createMimeMessage()).willReturn(mimeMessage);

        // When
        var emailFuture = emailService.send(expectedEmail);

        // Then
        var actualEmail = emailFuture.get();
        assertThat(actualEmail).isEqualToComparingFieldByField(expectedEmail);
    }
}
