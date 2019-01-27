package uk.co.serin.thule.utils.oauth2;

import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.test.util.ReflectionTestUtils;

import uk.co.serin.thule.utils.service.oauth2.DelegatingSecurityContextHolder;

import feign.RequestTemplate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class OAuth2FeignRequestInterceptorTest {

    private OAuth2FeignRequestInterceptor sut = new OAuth2FeignRequestInterceptor();

    @Test
    public void when_authentication_is_not_null_and_instanceof_oauth2authenticationdetails_then_authorization_header_is_added_to_request_template() {
        //Given
        var requestTemplate = mock(RequestTemplate.class);
        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken("", "");
        var oAuth2AuthenticationDetails = new OAuth2AuthenticationDetails(new MockHttpServletRequest());

        usernamePasswordAuthenticationToken.setDetails(oAuth2AuthenticationDetails);

        var securityContext = new SecurityContextImpl(usernamePasswordAuthenticationToken);

        var delegatingSecurityContextHolder = new DelegatingSecurityContextHolder();
        delegatingSecurityContextHolder.setContext(securityContext);
        ReflectionTestUtils.setField(sut, "delegatingSecurityContextHolder", delegatingSecurityContextHolder);


        //When
        sut.apply(requestTemplate);

        //Then
        verify(requestTemplate).header(HttpHeaders.AUTHORIZATION, String.format("%s %s", "Bearer", oAuth2AuthenticationDetails.getTokenValue()));
    }

    @Test
    public void when_authentication_is_not_null_and_not_instanceof_oauth2authenticationdetails_then_authorization_header_is_not_added_to_request_template() {
        //Given
        var requestTemplate = mock(RequestTemplate.class);
        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken("", "");
        var securityContext = new SecurityContextImpl(usernamePasswordAuthenticationToken);

        var delegatingSecurityContextHolder = new DelegatingSecurityContextHolder();
        delegatingSecurityContextHolder.setContext(securityContext);
        ReflectionTestUtils.setField(sut, "delegatingSecurityContextHolder", delegatingSecurityContextHolder);

        //When
        sut.apply(requestTemplate);

        //Then
        verifyZeroInteractions(requestTemplate);
    }

    @Test
    public void when_authentication_is_null_and_not_instanceof_oauth2authenticationdetails_then_authorization_header_is_not_added_to_request_template() {
        //Given
        var requestTemplate = mock(RequestTemplate.class);
        var securityContext = new SecurityContextImpl();

        var delegatingSecurityContextHolder = new DelegatingSecurityContextHolder();
        delegatingSecurityContextHolder.setContext(securityContext);
        ReflectionTestUtils.setField(sut, "delegatingSecurityContextHolder", delegatingSecurityContextHolder);

        //When
        sut.apply(requestTemplate);

        //Then
        verifyZeroInteractions(requestTemplate);
    }

}