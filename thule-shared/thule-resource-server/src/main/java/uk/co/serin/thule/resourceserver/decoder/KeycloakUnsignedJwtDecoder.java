package uk.co.serin.thule.resourceserver.decoder;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import uk.co.serin.thule.resourceserver.converter.KeycloakSubjectClaimConverter;
import uk.co.serin.thule.resourceserver.utils.KeycloakJwtUtils;

public class KeycloakUnsignedJwtDecoder implements JwtDecoder {
    @Override
    public Jwt decode(String token) {
        // Create raw keycloak Jwt
        var keycloakJwt = KeycloakJwtUtils.createKeycloakJwt(token);
        // Create adapter to convert keycloak username claim to the OAUth2 standard expected by Spring Security
        var keycloakSubjectClaimAdapter = new KeycloakSubjectClaimConverter();

        var headers = keycloakJwt.getHeaders();
        var claims = keycloakSubjectClaimAdapter.convert(keycloakJwt.getClaims());

        // Return a Jwt in a format expected by Spring Security
        return Jwt.withTokenValue(token)
                  .headers(h -> h.putAll(headers))
                  .claims(c -> c.putAll(claims)).build();
    }
}
