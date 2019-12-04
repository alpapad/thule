package uk.co.serin.thule.security.oauth2.feign;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import uk.co.serin.thule.security.context.DelegatingSecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class JwtPropagatingOAuth2FeignRequestInterceptorTest {
    @Mock
    private Authentication authentication;
    @Mock
    private DelegatingSecurityContextHolder delegatingSecurityContextHolder;
    @Mock
    private OAuth2AccessToken oAuth2AccessToken;
    @Mock
    private OAuth2AuthenticationDetails oAuth2AuthenticationDetails;
    @Mock
    private OAuth2ClientContext oAuth2ClientContext;
    @InjectMocks
    private JwtPropagatingOAuth2FeignRequestInterceptor sut;

    @Test
    public void when_authentication_exists_and_details_is_an_instanceof_oauth2authenticationdetails_then_a_token_is_returned() {
        //Given
        var token = "token";
        var expectedBearerToken = OAuth2FeignRequestInterceptor.BEARER + " " + "token";
        given(delegatingSecurityContextHolder.getAuthentication()).willReturn(authentication);
        given(authentication.getDetails()).willReturn(oAuth2AuthenticationDetails);
        given(oAuth2AuthenticationDetails.getTokenValue()).willReturn(token);

        //When
        var actualBearerToken = sut.extract(OAuth2FeignRequestInterceptor.BEARER);

        //Then
        assertThat(actualBearerToken).isEqualTo(expectedBearerToken);
    }

    @Test
    public void when_authentication_does_not_exist_then_processing_is_delegated_to_super_class() {
        //Given
        var token = "token";
        var expectedBearerToken = OAuth2FeignRequestInterceptor.BEARER + " " + "token";
        given(delegatingSecurityContextHolder.getAuthentication()).willReturn(null);
        given(oAuth2ClientContext.getAccessToken()).willReturn(oAuth2AccessToken);
        given(oAuth2AccessToken.getValue()).willReturn(token);

        //When
        var actualBearerToken = sut.extract(OAuth2FeignRequestInterceptor.BEARER);

        //Then
        assertThat(actualBearerToken).isEqualTo(expectedBearerToken);
    }

    @Test
    public void when_authentication_exists_and_details_is_not_an_instanceof_oauth2authenticationdetails_then_processing_is_delegated_to_super_class() {
        //Given
        var token = "token";
        var expectedBearerToken = OAuth2FeignRequestInterceptor.BEARER + " " + "token";
        given(delegatingSecurityContextHolder.getAuthentication()).willReturn(authentication);
        given(authentication.getDetails()).willReturn(new Object());
        given(oAuth2ClientContext.getAccessToken()).willReturn(oAuth2AccessToken);
        given(oAuth2AccessToken.getValue()).willReturn(token);

        //When
        var actualBearerToken = sut.extract(OAuth2FeignRequestInterceptor.BEARER);

        //Then
        assertThat(actualBearerToken).isEqualTo(expectedBearerToken);
    }
}