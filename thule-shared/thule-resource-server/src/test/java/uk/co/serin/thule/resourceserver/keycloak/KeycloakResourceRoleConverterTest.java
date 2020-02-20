package uk.co.serin.thule.resourceserver.keycloak;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class KeycloakResourceRoleConverterTest {
    public static final String RESOURCE_ID = "thule-test-service";
    @Mock
    private Environment environment;
    @Mock
    private Jwt jwt;
    @InjectMocks
    private KeycloakResourceRoleConverter sut;

    @Test
    public void given_jwt_with_no_claims_when_convert_jwt_then_empty_granted_authorities_are_returned() {
        // Given
        given(jwt.getClaims()).willReturn(null);

        // When
        var grantedAuthorities = sut.convert(jwt);

        //Then
        assertThat(grantedAuthorities).isEmpty();
    }

    @Test
    public void given_jwt_with_no_roles_when_convert_jwt_then_empty_granted_authorities_are_returned() {
        // Given
        var claims = Map.<String, Object>of();

        given(jwt.getClaims()).willReturn(claims);
        given(environment.getProperty("spring.application.name", "default")).willReturn("default");
        given(environment.getProperty("thule.shared.oauth2.resource.id", "default")).willReturn(RESOURCE_ID);

        // When
        var grantedAuthorities = sut.convert(jwt);

        //Then
        assertThat(grantedAuthorities).isEmpty();
    }

    @Test
    public void given_jwt_with_realm_level_roles_when_convert_jwt_then_empty_granted_authorities_are_returned() {
        // Given
        var realmAccess = Map.<String, Object>of("roles", List.of("PUBLIC"));
        var claims = Map.<String, Object>of("realm_access", realmAccess);

        given(jwt.getClaims()).willReturn(claims);
        given(environment.getProperty("spring.application.name", "default")).willReturn("default");
        given(environment.getProperty("thule.shared.oauth2.resource.id", "default")).willReturn(RESOURCE_ID);

        // When
        var grantedAuthorities = sut.convert(jwt);

        //Then
        assertThat(grantedAuthorities).containsExactly(grantedAuthorities.toArray(GrantedAuthority[]::new));
    }

    @Test
    public void given_jwt_with_resource_level_roles_for_required_resource_when_convert_jwt_then_granted_authorities_are_returned() {
        // Given
        var roles = Map.of("roles", List.of("PUBLIC"));
        var resourceAccess = Map.<String, Object>of(RESOURCE_ID, roles);
        var claims = Map.<String, Object>of("resource_access", resourceAccess);

        given(jwt.getClaims()).willReturn(claims);
        given(environment.getProperty("spring.application.name", "default")).willReturn("default");
        given(environment.getProperty("thule.shared.oauth2.resource.id", "default")).willReturn(RESOURCE_ID);

        // When
        var grantedAuthorities = sut.convert(jwt);

        //Then
        assertThat(grantedAuthorities).containsExactly(grantedAuthorities.toArray(GrantedAuthority[]::new));
    }

    @Test
    public void given_jwt_with_resource_level_roles_for_unrequired_resource_when_convert_jwt_then_empty_granted_authorities_are_returned() {
        // Given
        var roles = Map.of("roles", List.of("PUBLIC"));
        var resourceAccess = Map.<String, Object>of("another-resource", roles);
        var claims = Map.<String, Object>of("resource_access", resourceAccess);

        given(jwt.getClaims()).willReturn(claims);
        given(environment.getProperty("spring.application.name", "default")).willReturn("default");
        given(environment.getProperty("thule.shared.oauth2.resource.id", "default")).willReturn(RESOURCE_ID);

        // When
        var grantedAuthorities = sut.convert(jwt);

        //Then
        assertThat(grantedAuthorities).isEmpty();
    }
}