package uk.co.serin.thule.people.service.email;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.co.serin.thule.people.domain.model.email.Email;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceClientAsyncTest {
    @Mock
    private EmailServiceClient emailServiceClient;
    @InjectMocks
    private EmailServiceClientAsync emailServiceClientAsync;

    @Test
    void when_sending_email_then_it_is_delegated_to_email_service_client_returning_a_future() {
        // Given
        var email = Email.builder().build();

        // When
        var future = emailServiceClientAsync.sendEmail(email);

        // Then
        assertThat(future).isNotNull();
        verify(emailServiceClient).sendEmail(email);
    }
}