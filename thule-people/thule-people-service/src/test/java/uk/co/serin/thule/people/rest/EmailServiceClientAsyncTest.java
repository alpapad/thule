package uk.co.serin.thule.people.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import uk.co.serin.thule.people.domain.model.email.Email;

import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceClientAsyncTest {
    @InjectMocks
    private EmailServiceClientAsync emailServiceClientAsync;
    @Mock
    private EmailServiceClient emailServiceClient;

    @Test
    public void when_sending_email_then_it_is_delegated_to_email_service_client_returning_a_future() {
        // Given
        var email = Email.builder().build();

        // When
        var future = emailServiceClientAsync.sendEmail(email);

        // Then
        assertThat(future).isInstanceOf(Future.class);
        verify(emailServiceClient).sendEmail(email);
    }
}