package uk.co.serin.thule.repository.mongodb.support;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RandomUsernameAuditorAwareTest {

    @Test
    public void getCurrentAuditorReturnsAnAuditor() {
        // Given
        var randomUsernameAuditorAware = new RandomUsernameAuditorAware();

        // When
        var currentAuditor = randomUsernameAuditorAware.getCurrentAuditor();

        // Then
        assertThat(currentAuditor).isNotEmpty();
    }
}