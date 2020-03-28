package uk.co.serin.thule.resourceserver.context;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import uk.co.serin.thule.security.context.DelegatingSecurityContextHolder;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class JwtUserAuthenticationSecurityContextTest {
    private static final int USER_ID = 1234567890;
    @Mock
    private Authentication authentication;
    @Mock
    private JwtAuthenticationToken jwtAuthenticationToken;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private DelegatingSecurityContextHolder delegatingSecurityContextHolder;
    @InjectMocks
    private JwtUserAuthenticationSecurityContext sut;

    @Test
    public void given_authentication_with_a_jwt_and_no_userid_when_getUserAuthentication_then_UserAuthentication_is_not_empty() {
        // Given
        var tokenAttributes = Map.<String, Object>of();
        given(delegatingSecurityContextHolder.getContext()).willReturn(securityContext);
        given(securityContext.getAuthentication()).willReturn(jwtAuthenticationToken);
        given(jwtAuthenticationToken.getTokenAttributes()).willReturn(tokenAttributes);

        // When
        var userAuthenticationOptional = sut.getUserAuthentication();

        //Then
        assertThat(userAuthenticationOptional).isEmpty();
    }

    @Test
    public void given_authentication_with_a_jwt_and_userid_when_getUserAuthentication_then_UserAuthentication_is_not_empty() {
        // Given
        var tokenAttributes = Map.<String, Object>of("user_id", USER_ID);
        given(delegatingSecurityContextHolder.getContext()).willReturn(securityContext);
        given(securityContext.getAuthentication()).willReturn(jwtAuthenticationToken);
        given(jwtAuthenticationToken.getTokenAttributes()).willReturn(tokenAttributes);

        // When
        var userAuthenticationOptional = sut.getUserAuthentication();

        //Then
        assertThat(userAuthenticationOptional).isNotEmpty();
        assertThat(userAuthenticationOptional.orElseThrow().getUserId()).isEqualTo(USER_ID);
    }

    @Test
    public void given_authentication_without_a_jwt_when_getUserAuthentication_then_UserAuthentication_is_empty() {
        // Given
        given(delegatingSecurityContextHolder.getContext()).willReturn(securityContext);
        given(securityContext.getAuthentication()).willReturn(authentication);

        // When
        var userAuthenticationOptional = sut.getUserAuthentication();

        //Then
        assertThat(userAuthenticationOptional).isEmpty();
    }
}