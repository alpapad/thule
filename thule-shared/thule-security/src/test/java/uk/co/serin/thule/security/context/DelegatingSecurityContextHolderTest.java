package uk.co.serin.thule.security.context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class DelegatingSecurityContextHolderTest {
    @Mock
    private SecurityContext anotherSecurityContext;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;
    @InjectMocks
    private DelegatingSecurityContextHolder sut;

    @Test
    public void given_context_is_set_when_authentication_is_retrieved_then_authentication_is_not_null() {
        // Given
        given(securityContext.getAuthentication()).willReturn(authentication);
        sut.setContext(securityContext);

        // When
        var actualAuthentication = sut.getAuthentication();

        // Then
        assertThat(actualAuthentication).isSameAs(authentication);
    }

    @Before
    public void setup() {
        sut.clearContext();
    }

    @Test
    public void when_context_is_cleared_then_authentication_is_null() {
        // Given
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
        sut.setContext(securityContext);

        // When
        var actualContext = sut.getContext();

        // Then
        assertThat(actualContext).isSameAs(actualContext);
    }

    @Test
    public void when_context_is_set_then_context_retrieved_is_equal_to_new_context_set() {
        // Given
        sut.setContext(securityContext);

        // When
        sut.setContext(anotherSecurityContext);

        // Then
        var actualContext = sut.getContext();
        assertThat(actualContext).isSameAs(anotherSecurityContext);
    }
}
