package com.gohenry.oauth2;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import static org.assertj.core.api.Assertions.assertThat;

public class DelegatingSecurityContextHolderTest {

    private DelegatingSecurityContextHolder sut = new DelegatingSecurityContextHolder();

    @Before
    public void setup() {
        sut.clearContext();
    }

    @Test
    public void when_context_is_cleared_then_authentication_is_null() {
        //Given
        Authentication authentication = new UsernamePasswordAuthenticationToken("username", "password");
        SecurityContext initialContext = new SecurityContextImpl(authentication);

        sut.setContext(initialContext);

        //When
        sut.clearContext();

        //Then
        assertThat(sut.getContext()).isNotEqualTo(initialContext);
        assertThat(sut.getContext().getAuthentication()).isNull();
    }

    @Test
    public void when_get_details_get_user_authentication_details_then_not_null() {
        //Given
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("username", "password");

        UserAuthenticationDetails userAuthenticationDetails = new UserAuthenticationDetails(999);
        authentication.setDetails(userAuthenticationDetails);
        OAuth2Request oAuth2Request = new OAuth2Request(null, "username", null,
                true, null, null, null, null, null);
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
        SecurityContext initialContext = new SecurityContextImpl(oAuth2Authentication);


//        given(((OAuth2Authentication)authentication).getUserAuthentication().getDetails()).willReturn(userAuthenticationDetails);


        sut.setContext(initialContext);

        //When
        UserAuthenticationDetails actual = sut.getUserAuthenticationDetails();

        //Then
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(userAuthenticationDetails);
    }

    @Test
    public void when_context_is_created_then_authentication_is_null() {
        //Given//When
        SecurityContext context = sut.createEmptyContext();

        //Then
        assertThat(context).isNotNull();
        assertThat(sut.getContext().getAuthentication()).isNull();
    }

    @Test
    public void when_context_is_retrieved_then_context_authentication_details_match_initial_authentication() {
        //Given
        Authentication authentication = new UsernamePasswordAuthenticationToken("username", "password");
        SecurityContext initialContext = new SecurityContextImpl(authentication);

        sut.setContext(initialContext);

        //When
        SecurityContext context = sut.getContext();

        //Then
        assertThat(context).isEqualTo(initialContext);
        assertThat(context.getAuthentication().getPrincipal()).isEqualTo(authentication.getPrincipal());
        assertThat(context.getAuthentication().getCredentials()).isEqualTo(authentication.getCredentials());
    }

    @Test
    public void when_context_is_set_and_authentication_is_retrieved_then_authentication_is_not_null() {
        //Given
        Authentication expectedAuthentication = new UsernamePasswordAuthenticationToken("username", "password");
        SecurityContext initialContext = new SecurityContextImpl(expectedAuthentication);
        sut.setContext(initialContext);

        //When
        Authentication actualAuthentication = sut.getAuthentication();

        //Then
        assertThat(actualAuthentication.getPrincipal()).isEqualTo(expectedAuthentication.getPrincipal());
        assertThat(actualAuthentication.getCredentials()).isEqualTo(expectedAuthentication.getCredentials());
    }

    @Test
    public void when_context_is_set_then_context_retrieved_is_equal_to_new_context_set() {
        //Given
        Authentication initialAuthentication = new UsernamePasswordAuthenticationToken("username", "password");
        SecurityContext initialContext = new SecurityContextImpl(initialAuthentication);
        sut.setContext(initialContext);

        Authentication newAuthentication = new UsernamePasswordAuthenticationToken("newUsername", "newPassword");
        SecurityContext newContext = new SecurityContextImpl(newAuthentication);

        //When
        sut.setContext(newContext);

        //Then
        SecurityContext context = sut.getContext();
        assertThat(context).isEqualTo(newContext);
        assertThat(context).isNotEqualTo(initialContext);
    }
}
