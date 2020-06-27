package uk.co.serin.thule.resourceserver.utils;

import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.PlainHeader;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.PlainJWT;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class KeycloakJwtUtils {
    public static Jwt createKeycloakJwt(String userName, long userId, Collection<? extends GrantedAuthority> grantedAuthorities, String resourceId) {
        var grantedAuthoritiesWithoutRolePrefix = grantedAuthorities
                .stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority().replace("ROLE_",
                        "")) // Remove ROLE_ prefix which is spring security specific and not required by keycloak
                .collect(Collectors.toList());

        var headers = new PlainHeader.Builder()
                .type(JOSEObjectType.JWT)
                .customParam("kid", UUID.randomUUID().toString()).build();

        var resource = Map.of("roles", grantedAuthoritiesWithoutRolePrefix);
        var resourceAccess = Map.of(resourceId, resource);

        var claims = new JWTClaimsSet.Builder()
                .expirationTime(Date.from(LocalDateTime.now().plusDays(10).toInstant(ZoneOffset.UTC)))
                .issueTime(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)))
                .issuer("http://localhost:8080/auth/realms/test-realm")
                .jwtID(UUID.randomUUID().toString())
                .claim("preferred_username", userName)
                .claim("resource_access", resourceAccess)
                .claim("user_id", userId).build();

        return createKeycloakJwt(new PlainJWT(headers, claims));
    }

    private static Jwt createKeycloakJwt(JWT parsedJwt) {
        // Extract headers
        var headers = parsedJwt.getHeader().toJSONObject();

        // Extract claims
        Map<String, Object> claims;
        try {
            var mappedJwtClaimSetConverter = MappedJwtClaimSetConverter.withDefaults(Collections.emptyMap());
            claims = mappedJwtClaimSetConverter.convert(parsedJwt.getJWTClaimsSet().getClaims());
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }

        // Create Jwt
        return Jwt.withTokenValue(parsedJwt.serialize())
                  .headers(h -> h.putAll(headers))
                  .claims(c -> c.putAll(claims))
                  .build();
    }

    public static Jwt createKeycloakJwt(String token) {
        try {
            var parsedJwt = JWTParser.parse(token);
            return createKeycloakJwt(parsedJwt);
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
    }
}
