package uk.co.serin.thule.utils.service.oauth2;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import uk.co.serin.thule.utils.oauth2.UserAuthenticationDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class DelegatingSecurityContextHolderTest {
    private DelegatingSecurityContextHolder sut = new DelegatingSecurityContextHolder();

    @Before
    public void setup() {
        sut.clearContext();
    }

    @Test
    public void when_authentication_is_not_ouath2authentication_then_a_security_exception_is_thrown() {
        // Given
        var expectedErrorMessage =
                "Security context authentication is the wrong type, expecting [OAuth2Authentication] but was [UsernamePasswordAuthenticationToken]";

        var securityContext = new SecurityContextImpl(new UsernamePasswordAuthenticationToken("principal", "credentials"));
        sut.setContext(securityContext);

        // When
        var throwable = catchThrowable(() -> sut.getUserAuthenticationDetails());

        // Then
        assertThat(throwable).isInstanceOf(SecurityException.class);
        assertThat(throwable.getMessage()).isEqualTo(expectedErrorMessage);
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

    @Test
    public void when_get_user_authentication_details_then_not_null() {
        // Given
        var expectedUserAuthenticationDetails = new UserAuthenticationDetails(999);

        var authentication = new UsernamePasswordAuthenticationToken("username", "password");
        authentication.setDetails(expectedUserAuthenticationDetails);

        var oAuth2Request = new OAuth2Request(null, "username", null, true, null, null, null, null, null);
        var oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);

        SecurityContext initialContext = new SecurityContextImpl(oAuth2Authentication);
        sut.setContext(initialContext);

        // When
        var actualUserAuthenticationDetails = sut.getUserAuthenticationDetails();

        // Then
        assertThat(actualUserAuthenticationDetails).isNotNull();
        assertThat(actualUserAuthenticationDetails).isEqualTo(expectedUserAuthenticationDetails);
    }

    @Test
    public void when_user_authentication_details_is_the_wrong_type_then_a_security_exception_is_thrown() {
        // Given
        var details = "user authentication details as a string";
        var expectedErrorMessage = String.format("OAuth2 user authentication details are invalid [%s]", details);

        var authentication = new UsernamePasswordAuthenticationToken("username", "password");
        authentication.setDetails(details);

        var oAuth2Request = new OAuth2Request(null, "username", null, true, null, null, null, null, null);
        var oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
        SecurityContext testSecurityContext = new SecurityContextImpl(oAuth2Authentication);

        sut.setContext(testSecurityContext);

        // When
        var throwable = catchThrowable(() -> sut.getUserAuthenticationDetails());

        // Then
        assertThat(throwable).isInstanceOf(SecurityException.class);
        assertThat(throwable.getMessage()).isEqualTo(expectedErrorMessage);
    }
}
