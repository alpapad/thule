package uk.co.serin.thule.data.audit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;

import uk.co.serin.thule.security.oauth2.context.DelegatingSecurityContextHolder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class SpringSecurityAuditorAwareTest {
    @Mock
    private Authentication authentication;
    @Mock
    private DelegatingSecurityContextHolder delegatingSecurityContextHolder;
    @InjectMocks
    private SpringSecurityAuditorAware sut;

    @Test
    public void given_empty_current_auditor_when_get_current_auditor_then_illegal_argument_exception_is_thrown() {
        // Given
        var principalName = "";
        given(delegatingSecurityContextHolder.getAuthentication()).willReturn(authentication);
        given(authentication.getName()).willReturn(principalName);

        // When
        var throwable = catchThrowable(() -> sut.getCurrentAuditor());

        // Then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
        assertThat(throwable).hasMessage("Principal name is empty");
    }

    @Test
    public void given_null_authentication_when_get_current_auditor_then_illegal_argument_exception_is_thrown() {
        // When
        var throwable = catchThrowable(() -> sut.getCurrentAuditor());

        // Then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
        assertThat(throwable).hasMessage("Authentication is null");
    }

    @Test
    public void when_get_current_auditor_then_current_auditor_is_returned() {
        // Given
        var name = "auditor";
        given(delegatingSecurityContextHolder.getAuthentication()).willReturn(authentication);
        given(authentication.getName()).willReturn(name);

        // When
        var currentAuditor = sut.getCurrentAuditor();

        // Then
        assertThat(currentAuditor).isEqualTo(Optional.of(name));
    }
}