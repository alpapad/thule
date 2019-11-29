package uk.co.serin.thule.security.oauth2.context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import uk.co.serin.thule.security.context.UserAuthenticationDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class Oauth2DelegatingSecurityContextHolderTest {
    @Mock
    private Authentication authentication;
    @Mock
    private OAuth2Authentication oAuth2Authentication;
    @Mock
    private SecurityContext securityContext;
    @InjectMocks
    private Oauth2DelegatingSecurityContextHolder sut;
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
}
