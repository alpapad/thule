package uk.co.serin.thule.security.oauth2.context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class DelegatingSecurityContextHolderTest {
    protected static final String PASSWORD = "password";
    protected static final String USER_NAME = "username";
    @Mock
    private SecurityContext anotherSecurityContext;
    @Mock
    private Authentication authentication;
    @Mock
    private OAuth2Authentication oAuth2Authentication;
    @Mock
    private SecurityContext securityContext;
    private DelegatingSecurityContextHolder sut = new DelegatingSecurityContextHolder();
    @Mock
    private UserAuthenticationDetails userAuthenticationDetails;

    @Test
    public void given_a_user_authentication_details_when_get_user_authentication_details_then_a_user_authentication_details_is_returned() {
        // Given
        given(securityContext.getAuthentication()).willReturn(oAuth2Authentication);
        given(oAuth2Authentication.getUserAuthentication()).willReturn(authentication);
        given(authentication.getDetails()).willReturn(userAuthenticationDetails);
        sut.setContext(securityContext);

        // When
        var userAuthenticationDetails = sut.getUserAuthenticationDetails();

        // Then
        assertThat(userAuthenticationDetails).isNotEmpty();
    }

    @Test
    public void given_an_invalid_user_authentication_details_when_get_user_authentication_details_then_an_empty_optional_is_returned() {
        // Given
        given(securityContext.getAuthentication()).willReturn(oAuth2Authentication);
        given(oAuth2Authentication.getUserAuthentication()).willReturn(authentication);
        given(authentication.getDetails()).willReturn(new Object());
        sut.setContext(securityContext);

        // When
        var userAuthenticationDetails = sut.getUserAuthenticationDetails();

        // Then
        assertThat(userAuthenticationDetails).isEmpty();
    }

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

    @Test
    public void given_no_user_authentication_details_when_get_user_authentication_details_then_empty_optional_is_returned() {
        // Given
        sut.setContext(securityContext);

        // When
        var userAuthenticationDetails = sut.getUserAuthenticationDetails();

        // Then
        assertThat(userAuthenticationDetails).isEmpty();
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
