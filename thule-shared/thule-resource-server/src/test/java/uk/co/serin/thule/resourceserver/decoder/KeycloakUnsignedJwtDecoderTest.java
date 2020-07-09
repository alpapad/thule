package uk.co.serin.thule.resourceserver.decoder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import uk.co.serin.thule.resourceserver.utils.KeycloakJwtUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class KeycloakUnsignedJwtDecoderTest {
    private static final Set<GrantedAuthority> GRANTED_AUTHORITIES = Set.of(new SimpleGrantedAuthority("ROLE_PUBLIC"));
    private static final String RESOURCE_ID = "thule-test-service";
    private static final int USER_ID = 1234567890;
    private static final String USER_NAME = "user";
    @InjectMocks
    private KeycloakUnsignedJwtDecoder sut;

    @Test
    @SuppressWarnings("unchecked")
    public void given_jwt_tokeb_value_when_decode_then_jwt_is_returned() throws MalformedURLException {
        // Given
        var jwtTokenValue = KeycloakJwtUtils.createKeycloakJwt(USER_NAME, USER_ID, GRANTED_AUTHORITIES, RESOURCE_ID).getTokenValue();

        // When
        var jwt = sut.decode(jwtTokenValue);

        // Then
        assertThat(jwt.getHeaders()).containsKey("kid");
        assertThat(jwt.getIssuedAt()).isNotNull();
        assertThat(jwt.getExpiresAt()).isEqualTo(jwt.getIssuedAt().plus(Duration.ofDays(10)));
        assertThat(jwt.getIssuer()).isEqualTo(new URL("http://localhost:8080/auth/realms/test-realm"));
        assertThat(jwt.getClaimAsString("jti")).isNotNull();
        assertThat(jwt.getClaimAsString("preferred_username")).isEqualTo(USER_NAME);
        assertThat(jwt.<Long>getClaim("user_id")).isEqualTo(USER_ID);
        assertThat(jwt.getClaimAsString("resource_access")).isNotNull();

        var resourceAccess = jwt.<Map<String, Object>>getClaim("resource_access");
        var resource = (Map<String, Object>) resourceAccess.get(RESOURCE_ID);
        var roles = (List<String>) resource.get("roles");
        assertThat(roles).containsExactly("PUBLIC");
    }
}