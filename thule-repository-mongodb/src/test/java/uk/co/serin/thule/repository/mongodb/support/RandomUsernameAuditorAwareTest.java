package uk.co.serin.thule.repository.mongodb.support;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RandomUsernameAuditorAwareTest {

    @Test
    public void getCurrentAuditorReturnsAnAuditor() {
        // Given
        RandomUsernameAuditorAware randomUsernameAuditorAware = new RandomUsernameAuditorAware();

        // When
        String currentAuditor = randomUsernameAuditorAware.getCurrentAuditor();

        // Then
        assertThat(currentAuditor).isNotEmpty();
    }
}