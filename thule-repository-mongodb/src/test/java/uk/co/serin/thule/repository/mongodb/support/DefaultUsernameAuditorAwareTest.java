package uk.co.serin.thule.repository.mongodb.support;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class DefaultUsernameAuditorAwareTest {
    @InjectMocks
    private DefaultUsernameAuditorAware randomUsernameAuditorAware;

    @Test
    public void getCurrentAuditorReturnsAnAuditor() {
        // When
        var currentAuditor = randomUsernameAuditorAware.getCurrentAuditor();

        // Then
        assertThat(currentAuditor).isNotEmpty();
    }
}