package uk.co.serin.thule.people.service.email;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import uk.co.serin.thule.people.domain.model.email.Email;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceClientAsyncTest {
    @Mock
    private EmailServiceClient emailServiceClient;
    @InjectMocks
    private EmailServiceClientAsync emailServiceClientAsync;

    @Test
    public void when_sending_email_then_it_is_delegated_to_email_service_client_returning_a_future() {
        // Given
        var email = Email.builder().build();

        // When
        var future = emailServiceClientAsync.sendEmail(email);

        // Then
        assertThat(future).isNotNull();
        verify(emailServiceClient).sendEmail(email);
    }
}