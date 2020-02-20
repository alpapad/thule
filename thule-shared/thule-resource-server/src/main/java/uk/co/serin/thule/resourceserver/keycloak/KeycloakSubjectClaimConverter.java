package uk.co.serin.thule.resourceserver.keycloak;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;

import java.util.Collections;
import java.util.Map;

public class KeycloakSubjectClaimConverter implements Converter<Map<String, Object>, Map<String, Object>> {
    private final MappedJwtClaimSetConverter delegate = MappedJwtClaimSetConverter.withDefaults(Collections.emptyMap());

    @Override
    public Map<String, Object> convert(Map<String, Object> claims) {
        var convertedClaims = delegate.convert(claims);
        var username = convertedClaims.getOrDefault("preferred_username", "").toString();
        convertedClaims.put("sub", username);

        return convertedClaims;
    }
}
