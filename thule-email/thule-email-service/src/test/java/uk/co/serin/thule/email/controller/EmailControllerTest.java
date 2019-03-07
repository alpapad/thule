package uk.co.serin.thule.email.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import uk.co.serin.thule.email.domain.model.Email;
import uk.co.serin.thule.email.service.EmailService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EmailControllerTest {
    @InjectMocks
    private EmailController emailController;
    @Mock
    private EmailService emailService;

    @Test
    public void create_an_email() {
        // Given
        var expectedEmail = Email.builder().from("from@test.co.uk").subject("This is a test email").build();

        // When
        var actualEmail = emailController.createEmail(expectedEmail);

        // Then
        verify(emailService).send(expectedEmail);
        assertThat(actualEmail).isEqualTo(expectedEmail);
    }
}
