package uk.co.serin.thule.security.oauth2.resourceserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import uk.co.serin.thule.security.oauth2.context.UserIdEnhancedUserAuthenticationConverter;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class JwtAccessTokenCustomizerTest {
    @Mock
    private UserIdEnhancedUserAuthenticationConverter userIdEnhancedUserAuthenticationConverter;
    @Mock
    private JwtAccessTokenConverter jwtAccessTokenConverter;
    @InjectMocks
    private JwtAccessTokenCustomizer sut;

    @Test
    public void given_any_jwt_claims_when_extracting_authentication_then_a_oauth2authentication_is_created() {
        // Given
        var jwtClaims = Map.<String, Object>of();

        // When
        var oAuth2Authentication = sut.extractAuthentication(jwtClaims);

        // Then
        assertThat(oAuth2Authentication).isNotNull();
    }

    @Test
    public void when_configure_converter_then_converter_has_access_token_converter_set() {
        // When
        sut.configure(jwtAccessTokenConverter);

        // Then
        verify(jwtAccessTokenConverter).setAccessTokenConverter(sut);
    }
}