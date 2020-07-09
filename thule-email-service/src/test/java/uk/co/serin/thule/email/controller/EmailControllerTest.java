package uk.co.serin.thule.email.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.co.serin.thule.email.domain.model.Email;
import uk.co.serin.thule.email.service.EmailService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailControllerTest {
    @InjectMocks
    private EmailController emailController;
    @Mock
    private EmailService emailService;

    @Test
    void when_creating_an_email_then_the_email_service_is_invoked_and_an_email_is_returned() {
        // Given
        var expectedEmail = Email.builder().from("from@test.co.uk").subject("This is a test email").build();

        // When
        var actualEmail = emailController.createEmail(expectedEmail);

        // Then
        verify(emailService).send(expectedEmail);
        assertThat(actualEmail).isEqualTo(expectedEmail);
    }
}
