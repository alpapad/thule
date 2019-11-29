package uk.co.serin.thule.security.oauth2.utils;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.JsonParserFactory;

import uk.co.serin.thule.security.oauth2.resourceserver.JwtAccessTokenCustomizer;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Oauth2Utils {
    public static OAuth2AccessToken createJwtOauth2AccessToken(String userName, long userId, Collection<? extends GrantedAuthority> grantedAuthorities,
                                                               String clientId, String signingKey) {
        var jwtAsMap = Map.of(
                "authorities", AuthorityUtils.authorityListToSet(grantedAuthorities),
                "client_id", clientId,
                "exp", LocalDateTime.now().plusDays(10).toInstant(ZoneOffset.UTC).toEpochMilli(),
                "jti", "bb4915fa-c73b-4185-b3db-367da36abec4",
                "scope", Set.of(),
                "user_id", userId,
                "user_name", userName);

        return convertJwtToOAuth2AccessToken(signingKey, jwtAsMap);
    }

    private static OAuth2AccessToken convertJwtToOAuth2AccessToken(String signingKey, Map<String, Object> jwtAsMap) {
        var jwtAsJson = JsonParserFactory.create().formatMap(jwtAsMap);
        var jwt = JwtHelper.encode(jwtAsJson, new MacSigner(signingKey)).getEncoded();

        return new DefaultOAuth2AccessToken(jwt);
    }

    public static OAuth2AccessToken createPhpJwtOauth2AccessToken(String userName, long userId, Collection<? extends GrantedAuthority> grantedAuthorities,
                                                                  String clientId, String signingKey) {
        var phpDataStructure = Map.of(
                JwtAccessTokenCustomizer.PHP_USER_ID, userId,
                JwtAccessTokenCustomizer.PHP_USER_NAME, userName);

        var jwtAsMap = Map.of(
                "authorities", AuthorityUtils.authorityListToSet(grantedAuthorities),
                "client_id", clientId,
                "data", phpDataStructure,
                "exp", LocalDateTime.now().plusDays(10).toInstant(ZoneOffset.UTC).toEpochMilli(),
                "jti", "bb4915fa-c73b-4185-b3db-367da36abec4",
                "scope", Set.of());

        return convertJwtToOAuth2AccessToken(signingKey, jwtAsMap);
    }
}
