package uk.co.serin.thule.people.repository.support;

import org.junit.After;
import org.junit.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import uk.co.serin.thule.people.datafactory.TestDataFactory;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class SpringSecurityAuditorAwareTest {
    @After
    public void tearDown() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Test
    public void when_get_current_auditor_then_an_auditor_is_returned() {
        // Given
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken(TestDataFactory.JUNIT_TEST_USERNAME, TestDataFactory.JUNIT_TEST_USERNAME));

        SpringSecurityAuditorAware springSecurityAuditorAware = new SpringSecurityAuditorAware();

        // When
        Optional<String> optionalCurrentAuditor = springSecurityAuditorAware.getCurrentAuditor();

        // Then
        String actualCurrentAuditor = optionalCurrentAuditor.orElseThrow();
        assertThat(actualCurrentAuditor).isEqualTo(TestDataFactory.JUNIT_TEST_USERNAME);
    }
}