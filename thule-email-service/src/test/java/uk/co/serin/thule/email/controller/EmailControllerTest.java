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

import javax.validation.ValidationException;

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
        String from = "from@test.co.uk";
        String subject = "subject";
        Email expectedEmail = new Email(from, subject);

        // When
        ResponseEntity<Email> responseEntity = emailController.createEmail(expectedEmail);

        // Then
        verify(emailService).createEmail(expectedEmail);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }

//    @Test
//    public void validation_exception_returns_bad_request() {
//        // Given
//        String expectedExceptionMessage = "Invalid Request";
//        ValidationException validationException = new ValidationException(expectedExceptionMessage);
//
//        // When
//        ResponseEntity<String> responseEntity = emailController.validationExceptionHandler(validationException);
//
//        // Then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
//        assertThat(responseEntity.getBody()).isEqualTo(expectedExceptionMessage);
//    }
}
