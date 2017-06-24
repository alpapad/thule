package uk.co.serin.thule.people.repository.support;

import org.junit.After;
import org.junit.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;

public class SpringSecurityAuditorAwareTest {
    private static final String AUDITOR = "JUnitTest";

    @Test
    public void get_current_auditor_returns_an_auditor() {
        // Given
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken(AUDITOR, "password"));

        SpringSecurityAuditorAware springSecurityAuditorAware = new SpringSecurityAuditorAware();

        // When
        String currentAuditor = springSecurityAuditorAware.getCurrentAuditor();

        // Then
        assertThat(currentAuditor).isEqualTo(AUDITOR);
    }

    @After
    public void tearDown() {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof CredentialsContainer) {
            CredentialsContainer.class.cast(SecurityContextHolder.getContext().getAuthentication()).eraseCredentials();
        }
    }
}