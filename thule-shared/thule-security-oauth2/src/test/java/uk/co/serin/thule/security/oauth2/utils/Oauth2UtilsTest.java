package uk.co.serin.thule.security.oauth2.utils;

import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class Oauth2UtilsTest {
    @Test
    public void when_createJwtOauth2AccessToken_then_an_instance_is_created() {
        // When
        var jwtOauth2AccessToken = Oauth2Utils
                .createJwtOauth2AccessToken("userName", 1234567890, Set.of(new SimpleGrantedAuthority("grantedAuthority")),
                        "clientId", "signingKey");

        // Then
        assertThat(jwtOauth2AccessToken).isNotNull();
    }

    @Test
    public void when_createPhpJwtOauth2AccessToken_then_an_instance_is_created() {
        // When
        var jwtOauth2AccessToken = Oauth2Utils
                .createPhpJwtOauth2AccessToken("userName", 1234567890, Set.of(new SimpleGrantedAuthority("grantedAuthority")),
                        "clientId", "signingKey");

        // Then
        assertThat(jwtOauth2AccessToken).isNotNull();
    }
}