package uk.co.serin.thule.repository.mongodb.support;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
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