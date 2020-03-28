package uk.co.serin.thule.resourceserver.utils;

import com.nimbusds.jose.PlainHeader;
import com.nimbusds.jwt.JWT;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class JwtUtilsTest {
    private static final Set<GrantedAuthority> GRANTED_AUTHORITIES = Set.of(new SimpleGrantedAuthority("ROLE_PUBLIC"));
    private static final String RESOURCE_ID = "thule-test-service";
    private static final int USER_ID = 1234567890;
    private static final String USER_NAME = "user";
    @Mock
    private JWT jwt;

    @Test
    @SuppressWarnings("unchecked")
    public void given_client_details_when_createKeycloakJwt_then_a_jwt_in_keycloak_format_is_returned() throws MalformedURLException {
        // When
        var jwt = JwtUtils.createKeycloakJwt(USER_NAME, USER_ID, GRANTED_AUTHORITIES, RESOURCE_ID);

        //Then
        assertThat(jwt.getHeaders()).containsKey("kid");
        assertThat(jwt.getIssuedAt()).isNotNull();
        assertThat(jwt.getExpiresAt()).isEqualTo(jwt.getIssuedAt().plus(Duration.ofDays(10)));
        assertThat(jwt.getIssuer()).isEqualTo(new URL("http://localhost:8080/auth/realms/test-realm"));
        assertThat(jwt.getClaimAsString("jti")).isNotNull();
        assertThat(jwt.getClaimAsString("preferred_username")).isEqualTo(USER_NAME);
        assertThat(jwt.<Long>getClaim("user_id")).isEqualTo(USER_ID);
        assertThat(jwt.getClaimAsString("resource_access")).isNotNull();

        var resourceAccess = jwt.<Map<String, Object>>getClaim("resource_access");
        var resource = (Map<String, Object>) resourceAccess.get("thule-test-service");
        var roles = (List<String>) resource.get("roles");
        assertThat(roles).containsExactly("PUBLIC");
    }

    @Test
    public void given_invalid_jwt_claimset_when_createKeycloakJwt_then_an_invalid_state_exception_is_thrown() throws ParseException {
        // Given
        given(jwt.getHeader()).willReturn(new PlainHeader());
        given(jwt.getJWTClaimsSet()).willThrow(ParseException.class);

        // When
        var illegalStateException =
                catchThrowableOfType(() -> ReflectionTestUtils.invokeMethod(JwtUtils.class, "createKeycloakJwt", jwt), IllegalStateException.class);

        //Then
        assertThat(illegalStateException).isNotNull();
    }

    @Test
    public void given_invalid_jwt_token_value_when_createKeycloakJwt_then_an_invalid_state_exception_is_thrown() {
        // Given
        var invalidJwtTokenValue = "invalidjwttokenvalue";

        // When
        var illegalStateException = catchThrowableOfType(() -> JwtUtils.createKeycloakJwt(invalidJwtTokenValue), IllegalStateException.class);

        //Then
        assertThat(illegalStateException).isNotNull();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void given_jwt_token_value_when_createKeycloakJwt_then_a_jwt_in_keycloak_format_is_returned() throws MalformedURLException {
        // Given
        var jwtTokenValue = JwtUtils.createKeycloakJwt(USER_NAME, USER_ID, GRANTED_AUTHORITIES, RESOURCE_ID).getTokenValue();

        // When
        var jwt = JwtUtils.createKeycloakJwt(jwtTokenValue);

        //Then
        assertThat(jwt.getHeaders()).containsKey("kid");
        assertThat(jwt.getIssuedAt()).isNotNull();
        assertThat(jwt.getExpiresAt()).isEqualTo(jwt.getIssuedAt().plus(Duration.ofDays(10)));
        assertThat(jwt.getIssuer()).isEqualTo(new URL("http://localhost:8080/auth/realms/test-realm"));
        assertThat(jwt.getClaimAsString("jti")).isNotNull();
        assertThat(jwt.getClaimAsString("preferred_username")).isEqualTo(USER_NAME);
        assertThat(jwt.<Long>getClaim("user_id")).isEqualTo(USER_ID);
        assertThat(jwt.getClaimAsString("resource_access")).isNotNull();

        var resourceAccess = jwt.<Map<String, Object>>getClaim("resource_access");
        var resource = (Map<String, Object>) resourceAccess.get("thule-test-service");
        var roles = (List<String>) resource.get("roles");
        assertThat(roles).containsExactly("PUBLIC");
    }
}