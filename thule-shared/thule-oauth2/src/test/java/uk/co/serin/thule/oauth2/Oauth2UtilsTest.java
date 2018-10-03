package uk.co.serin.thule.oauth2;

import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class Oauth2UtilsTest {
    @Test
    public void jwtOauth2AccessToken_can_be_created() {
        // Given

        // When
        OAuth2AccessToken jwtOauth2AccessToken = Oauth2Utils.createJwtOauth2AccessToken(
                "userName", "password", 1234567890,
                Collections.singleton(new SimpleGrantedAuthority("grantedAuthority")),
                "clientId", "signingKey");

        // Then
        assertThat(jwtOauth2AccessToken).isNotNull();
    }
}