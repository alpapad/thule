package uk.co.serin.thule.email.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import uk.co.serin.thule.email.domain.Email;
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
        Email expectedEmail = Email.builder().from("from@test.co.uk").subject("This is a test email").build();

        // When
        ResponseEntity<Email> responseEntity = emailController.createEmail(expectedEmail);

        // Then
        verify(emailService).createEmail(expectedEmail);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }
}
