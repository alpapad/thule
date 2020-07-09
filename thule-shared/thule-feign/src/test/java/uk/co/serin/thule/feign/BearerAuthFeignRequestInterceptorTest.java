package uk.co.serin.thule.feign;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import uk.co.serin.thule.security.context.DelegatingSecurityContextHolder;

import feign.RequestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BearerAuthFeignRequestInterceptorTest {
    public static final String JWT_TOKEN_VALUE = "tokenvalue";
    @Mock
    private Authentication authentication;
    @Mock
    private DelegatingSecurityContextHolder delegatingSecurityContextHolder;
    @Mock
    private Jwt jwt;
    @Mock
    private JwtAuthenticationToken jwtAuthenticationToken;
    @Mock
    private OAuth2AccessToken oAuth2AccessToken;
    @Mock
    private OAuth2AuthorizedClient oAuth2AuthorizedClient;
    @Mock
    private OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;
    @Mock
    private RequestTemplate requestTemplate;
    @InjectMocks
    private BearerAuthFeignRequestInterceptor sut;

    @Test
    void given_an_authentication_with_a_jwt_when_getUserAuthentication_then_a_bearer_auth_header_is_added_to_the_requestTemplate() {
        // Given
        given(delegatingSecurityContextHolder.getAuthentication()).willReturn(jwtAuthenticationToken);
        given(jwtAuthenticationToken.getToken()).willReturn(jwt);
        given(jwt.getTokenValue()).willReturn(JWT_TOKEN_VALUE);

        // When
        sut.apply(requestTemplate);

        // Then
        verify(requestTemplate).header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN_VALUE);
    }

    @Test
    void given_an_authentication_without_a_jwt_when_getUserAuthentication_then_a_bearer_auth_header_is_added_to_the_requestTemplate() {
        // Given
        given(delegatingSecurityContextHolder.getAuthentication()).willReturn(authentication);
        given(oAuth2AuthorizedClientManager.authorize(any())).willReturn(oAuth2AuthorizedClient);
        given(oAuth2AuthorizedClient.getAccessToken()).willReturn(oAuth2AccessToken);
        given(oAuth2AccessToken.getTokenValue()).willReturn(JWT_TOKEN_VALUE);

        // When
        sut.apply(requestTemplate);

        // Then
        verify(requestTemplate).header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN_VALUE);
    }
}