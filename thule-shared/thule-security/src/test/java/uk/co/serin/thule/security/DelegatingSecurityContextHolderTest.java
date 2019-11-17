package uk.co.serin.thule.security;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextImpl;

import static org.assertj.core.api.Assertions.assertThat;

public class DelegatingSecurityContextHolderTest {
    private DelegatingSecurityContextHolder sut = new DelegatingSecurityContextHolder();

    @Before
    public void setup() {
        sut.clearContext();
    }

    @Test
    public void when_context_is_cleared_then_authentication_is_null() {
        // Given
        var authentication = new UsernamePasswordAuthenticationToken("username", "password");
        var securityContext = new SecurityContextImpl(authentication);

        sut.setContext(securityContext);

        // When
        sut.clearContext();

        // Then
        assertThat(sut.getContext()).isNotEqualTo(securityContext);
        assertThat(sut.getContext().getAuthentication()).isNull();
    }

    @Test
    public void when_context_is_created_then_authentication_is_null() {
        // When
        var context = sut.createEmptyContext();

        // Then
        assertThat(context).isNotNull();
        assertThat(sut.getContext().getAuthentication()).isNull();
    }

    @Test
    public void when_context_is_retrieved_then_context_authentication_details_match_initial_authentication() {
        // Given
        var expectedAuthentication = new UsernamePasswordAuthenticationToken("username", "password");
        var expectedContext = new SecurityContextImpl(expectedAuthentication);

        sut.setContext(expectedContext);

        // When
        var actualContext = sut.getContext();

        // Then
        assertThat(actualContext).isEqualTo(expectedContext);
        assertThat(actualContext.getAuthentication().getPrincipal()).isEqualTo(expectedAuthentication.getPrincipal());
        assertThat(actualContext.getAuthentication().getCredentials()).isEqualTo(expectedAuthentication.getCredentials());
    }

    @Test
    public void when_context_is_set_and_authentication_is_retrieved_then_authentication_is_not_null() {
        // Given
        var expectedAuthentication = new UsernamePasswordAuthenticationToken("username", "password");
        var initialContext = new SecurityContextImpl(expectedAuthentication);
        sut.setContext(initialContext);

        // When
        var actualAuthentication = sut.getAuthentication();

        // Then
        assertThat(actualAuthentication.getPrincipal()).isEqualTo(expectedAuthentication.getPrincipal());
        assertThat(actualAuthentication.getCredentials()).isEqualTo(expectedAuthentication.getCredentials());
    }

    @Test
    public void when_context_is_set_then_context_retrieved_is_equal_to_new_context_set() {
        // Given
        var initialAuthentication = new UsernamePasswordAuthenticationToken("username", "password");
        var initialContext = new SecurityContextImpl(initialAuthentication);
        sut.setContext(initialContext);

        var newAuthentication = new UsernamePasswordAuthenticationToken("newUsername", "newPassword");
        var expectedContext = new SecurityContextImpl(newAuthentication);

        // When
        sut.setContext(expectedContext);

        // Then
        var actualContext = sut.getContext();
        assertThat(actualContext).isEqualTo(expectedContext);
        assertThat(actualContext).isNotEqualTo(initialContext);
    }
}
